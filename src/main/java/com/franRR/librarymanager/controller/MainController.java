package com.franRR.librarymanager.controller;

import com.franRR.librarymanager.model.Category;
import com.franRR.librarymanager.services.BookService;
import com.franRR.librarymanager.services.CategoryService;
import com.franRR.librarymanager.services.impl.BookServiceImpl;
import com.franRR.librarymanager.services.impl.CategoryServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Loan;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class MainController {

    // --- PESTAÑA 1: LIBROS Y CATEGORÍAS ---
    @FXML
    private TextField searchBookField;
    @FXML private TableView<Book> bookTableView;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, Category> categoryColumn;
    @FXML private TableColumn<Book, Integer> availableQuantityColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, java.time.LocalDate> yearColumn;

    @FXML private TextField isbnField;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField stockField;
    @FXML private DatePicker yearField;
    @FXML private Button saveBookButton;

    @FXML private TextField newCategoryField;
    @FXML private Button addCategoryButton;

    // --- PESTAÑA 2: PRÉSTAMOS ACTIVOS ---
    @FXML private TextField searchLoanField;
    @FXML private Button registerLoanButton;
    @FXML private TableView<Loan> loanTableView;
    @FXML private TableColumn<?, ?> loanTitleColumn;
    @FXML private TableColumn<?, ?> loanDateColumn;
    @FXML private TableColumn<?, ?> dueDateColumn;
    @FXML private TableColumn<?, ?> actionColumn;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final CategoryService categoryService = new CategoryServiceImpl();
    private final BookService bookService = new BookServiceImpl();


    @FXML
    public void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        yearColumn.setCellFactory(column -> new TableCell<Book, java.time.LocalDate>() {
            @Override
            protected void updateItem(java.time.LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));



        bookTableView.setItems(bookList);
        refreshCategories();
        refreshBooks();

    }

    @FXML
    private void handleAddCategory(){
        String categoryName = newCategoryField.getText().trim();

        if(categoryName.isEmpty()){
            // Mosytrar mensaje de error de que el campo está vacío
            showAlert(Alert.AlertType.WARNING, "Campos vacíos", "No se puede añadir la categoría", "Por favor, escribe un nombre para la categoría.");
            return;
        }

        for( Category cat : categoryComboBox.getItems()){
            if(cat.getName().equalsIgnoreCase(categoryName)){
                // Mostrar mensaje de error de que la categoría ya existe
                showAlert(Alert.AlertType.ERROR, "Categoría duplicada", "La categoría ya existe", "Ya hay una categoría registrada con el nombre: " + categoryName);
                newCategoryField.clear();
                return;
            }
        }

        Category newCategory = new Category();
        newCategory.setName(categoryName);
        categoryService.save(newCategory);


        newCategoryField.clear();
        //CATEGORÍA GUARDADA
        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Categoría guardada", "La categoría '" + categoryName + "' se ha creado correctamente.");
        refreshCategories();
    }

    @FXML
    private void handleSaveBook() {
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        java.time.LocalDate selectedDate = yearField.getValue();
        Category selectedCategory = categoryComboBox.getValue();


        if( isbn.isEmpty() ||title.isEmpty() || author.isEmpty() || selectedCategory == null || selectedDate == null){
            // Mostrar mensaje de error de campos vacíos o no válidos
            showAlert(Alert.AlertType.WARNING, "Campos incorrectos", "Faltan datos o son inválidos",
                    "Por favor, asegúrate de rellenar todos los campos. Verifica que la fecha escrita a mano tenga el formato dd/MM/yyyy.");
            return;
        }

        try{
            int stock = Integer.parseInt(stockField.getText().trim());
            if(stock < 0){
                showAlert(Alert.AlertType.ERROR, "Stock inválido", "Cantidad negativa", "El stock disponible no puede ser menor que 0.");
                return;
            }

            bookService.save(new Book(isbn, title, author, selectedDate, stock, selectedCategory));

            isbnField.clear();
            titleField.clear();
            authorField.clear();
            categoryComboBox.setValue(null);
            stockField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Libro registrado", "El libro '" + title + "' ha sido guardado con éxito.");
            refreshBooks();
        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Error de formato", "Stock incorrecto", "El campo de cantidad debe ser un número entero válido.");        }




    }

    private void refreshCategories(){
        List<Category> categories = categoryService.findAll();
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
    }

    public void refreshBooks(){
        List<Book> books = bookService.findAll();
        bookList.clear();
        bookList.addAll(books);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
