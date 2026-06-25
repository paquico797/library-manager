package com.franRR.librarymanager.controller;

import com.franRR.librarymanager.model.*;
import com.franRR.librarymanager.services.BookService;
import com.franRR.librarymanager.services.CategoryService;
import com.franRR.librarymanager.services.impl.BookServiceImpl;
import com.franRR.librarymanager.services.impl.CategoryServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;


public class MainController {

    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private final CategoryService categoryService = new CategoryServiceImpl();
    private final BookService bookService = new BookServiceImpl();




    private final com.franRR.librarymanager.services.UserService userService = new com.franRR.librarymanager.services.impl.UserServiceImpl();
    private javafx.collections.ObservableList<User> customerList = FXCollections.observableArrayList();




    // Listas auxiliares para los filtros dinámicos en tiempo real
    private final javafx.collections.transformation.FilteredList<Book> filteredLoanBooks = new javafx.collections.transformation.FilteredList<>(bookList, p -> true);
    private final javafx.collections.transformation.FilteredList<User> filteredLoanCustomers = new javafx.collections.transformation.FilteredList<>(customerList, p -> true);

    private final com.franRR.librarymanager.services.EmployeeService employeeService = new com.franRR.librarymanager.services.impl.EmployeeServiceImpl();
    private javafx.collections.ObservableList<Employee> employeeList = FXCollections.observableArrayList();



    private final com.franRR.librarymanager.services.LoanService loanService = new com.franRR.librarymanager.services.impl.LoanServiceImpl();
    private final javafx.collections.ObservableList<Loan> loanList = javafx.collections.FXCollections.observableArrayList();
    // --- PESTAÑA 1: LIBROS Y CATEGORÍAS ---
    @FXML private TextField searchBookField;
    @FXML private TableView<Book> bookTableView;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> categoryColumn;
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
    @FXML private Button clearButton;
    @FXML private ListView<Category> categoryListView;
    @FXML private TextField newCategoryField;
    @FXML private Button addCategoryButton;

    // --- PESTAÑA 2: GESTIÓN UNIFICADA DE PRÉSTAMOS ---
    @FXML private TableView<Loan> loanTableView;
    @FXML private TableColumn<Loan, String> loanTitleColumn;
    @FXML private TableColumn<Loan, java.time.LocalDate> loanDateColumn;
    @FXML private TableColumn<Loan, java.time.LocalDate> dueDateColumn;
    @FXML private TableColumn<Loan, String> stateColumn;
    @FXML private TableColumn<Loan, Void> actionColumn;
    @FXML private TextField searchBookLoanField;
    @FXML private TableColumn<Book, String> loanBookTitleColumn;
    @FXML private TableColumn<Book, String> loanIsbnBookColumn;
    @FXML private TextField searchCustomerLoanField;
    @FXML private TableColumn<User, String> dniLoanColumn;
    @FXML private TableColumn<User, String> nameLoanColumn;
    @FXML private Button registerLoanButton;

    // --- PESTAÑA 3: GESTIÓN DE CLIENTES Y SU HISTORIAL ---
    @FXML private TableView<User> customerTable;
    @FXML private TableColumn<User, String> dniColumn;
    @FXML private TableColumn<User, String> customerNameColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> secondNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, Void> accionesColumn;
    @FXML private TableView<Loan> loanTbleView1;
    @FXML private TableColumn<Loan, String> loanTitleColumn1;
    @FXML private TableColumn<Loan, java.time.LocalDate> loanDateColumn1;
    @FXML private TableColumn<Loan, java.time.LocalDate> dueDateColumn1;
    @FXML private TextField customerNameField;
    @FXML private TextField customerFirstNameField;
    @FXML private TextField customerSecondNameField;
    @FXML private TextField customerDniField;
    @FXML private TextField customerEmailField;
    @FXML private TextField customerPhoneField;
    @FXML private Button customerCleanBtn;
    @FXML private Button saveCustomerBtn;
    @FXML
    private TableView<Book> loanBookTableView;
    @FXML
    private TableView<User> loanCustomerTable;

    private final javafx.collections.transformation.FilteredList<Book> filteredMainBooks = new javafx.collections.transformation.FilteredList<>(bookList, p -> true);

    //--- PESTAÑA 4: GESTIÓN DE EMPLEADOS ---
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, Long> idColumn;
    @FXML
    private TableColumn<Employee, String> employeeUserColumn;
    @FXML
    private TableColumn<Employee, String> rolColumn;
    @FXML
    private TableColumn<Employee, Void> employeeActionColumn;
    @FXML
    private TextField userEmployeeField;
    @FXML
    private TextField passwdEmployeeField;
    @FXML
    private ComboBox<String> rolComboBox;
    @FXML
    private Button clearEmployeeFieldsButton;
    @FXML
    private Button saveEmployeeButton;








    public void initialize() {
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory() != null ?
                        cellData.getValue().getCategory().getName() : "Sin categoría")
        );
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        yearColumn.setCellFactory(column -> new TableCell<Book, LocalDate>() {
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

        categoryListView.setCellFactory(param -> new ListCell<Category>() {
            private final Button deleteButton = new Button("X");
            private final Label categoryNameLabel = new Label();
            private final javafx.scene.layout.HBox layout = new javafx.scene.layout.HBox();

            {
                // Añadimos el texto y el botón dentro de un contenedor horizontal (HBox)
                layout.getChildren().addAll(categoryNameLabel, deleteButton);
                javafx.scene.layout.HBox.setHgrow(categoryNameLabel, javafx.scene.layout.Priority.ALWAYS);
                layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                layout.setSpacing(10);

                // Estilo visual rojo para la "X" como en tu boceto
                deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                categoryNameLabel.setMaxWidth(Double.MAX_VALUE);

                // Acción al pulsar el botón "X"
                deleteButton.setOnAction(event -> {
                    Category category = getItem();
                    if (category != null) {
                        categoryService.delete(category);
                        showAlert(Alert.AlertType.INFORMATION, "Eliminado", "Categoría borrada",
                                "La categoría '" + category.getName() + "' ha sido eliminada.");
                        refreshCategories();
                    }
                });
            }

            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setGraphic(null);
                } else {
                    categoryNameLabel.setText(category.getName());
                    setGraphic(layout);
                }
            }
        });

// === 1. CAMBIO: ASIGNAR LA LISTA FILTRADA EN LUGAR DE LA LISTA ORIGINAL ===
        bookTableView.setItems(filteredMainBooks);

// === 2. CAMBIO: AÑADIR EL ESCUCHADOR PARA LA BARRA DE BÚSQUEDA ===
        searchBookField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMainBooks.setPredicate(book -> {
                // Si el buscador está vacío, mostramos todos los libros del catálogo
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();

                // Criterios de filtrado dinámico
                boolean matchTitle = book.getTitle() != null && book.getTitle().toLowerCase().contains(lowerCaseFilter);
                boolean matchAuthor = book.getAuthor() != null && book.getAuthor().toLowerCase().contains(lowerCaseFilter);
                boolean matchIsbn = book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lowerCaseFilter);
                boolean matchCategory = book.getCategory() != null && book.getCategory().getName() != null
                        && book.getCategory().getName().toLowerCase().contains(lowerCaseFilter);

                // Si coincide con título, autor, isbn o categoría, mantiene el libro visible
                return matchTitle || matchAuthor || matchIsbn || matchCategory;
            });
        });
        refreshCategories();
        refreshBooks();

        // Configuración de columnas de Clientes (Mapeo exacto con User.java)
        dniColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        // Mapeo inteligente para separar el lastName de la base de datos en tus dos columnas visuales
        firstNameColumn.setCellValueFactory(cellData -> {
            String completos = cellData.getValue().getLastName();
            String primerapellido = (completos != null && completos.contains(" ")) ? completos.split(" ")[0] : completos;
            return new javafx.beans.property.SimpleStringProperty(primerapellido);
        });

        secondNameColumn.setCellValueFactory(cellData -> {
            String completos = cellData.getValue().getLastName();
            String segundoapellido = (completos != null && completos.contains(" ")) ? completos.substring(completos.indexOf(" ") + 1) : "";
            return new javafx.beans.property.SimpleStringProperty(segundoapellido);
        });

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));




        accionesColumn.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button deleteButton = new Button("Eliminar");
            {
                deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        userService.delete(user.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado", "El cliente ha sido dado de baja.");
                        refreshCustomers();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(deleteButton);
            }
        });

        customerTable.setItems(customerList);
        refreshCustomers();

        loanTitleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBook().getTitle())
        );

        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Formatear las columnas de fechas para que sean legibles (dd/MM/yyyy)
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        loanDateColumn.setCellFactory(col -> new TableCell<Loan, LocalDate>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        dueDateColumn.setCellFactory(col -> new TableCell<Loan, LocalDate>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        // Configuración del botón "Devolver" dinámico en la columna de acciones
        actionColumn.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button returnButton = new Button("Devolver");
            {
                returnButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-cursor: hand;");
                returnButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan != null) {
                        loanService.returnBook(loan);
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Libro Devuelto", "El libro ha sido reincorporado al inventario.");
                        refreshLoans();
                        refreshBooks(); // Refresca el stock en la tabla de libros
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(returnButton);
            }
        });

        // Enlazar la lista observable a la tabla de préstamos
        loanTableView.setItems(loanList);
        refreshLoans();

// === CONFIGURACIÓN SECCIÓN PRÉSTAMOS UNIFICADOS ===

// A. Tabla de Libros Disponibles (Derecha Superior)
        loanBookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        loanIsbnBookColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        loanBookTableView.setItems(filteredLoanBooks);

// Escucha del Buscador de Libros (Filtra de manera óptima por Título o por ISBN)
        searchBookLoanField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLoanBooks.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().trim();

                boolean matchTitle = book.getTitle() != null && book.getTitle().toLowerCase().contains(lowerCaseFilter);
                boolean matchIsbn = book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lowerCaseFilter);

                return matchTitle || matchIsbn;
            });
        });

// B. Tabla de Clientes (DerechaInferior)
        dniLoanColumn.setCellValueFactory(new PropertyValueFactory<>("dni"));
        nameLoanColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        loanCustomerTable.setItems(filteredLoanCustomers); // Asegúrate de que use loanCustomerTableView según tus campos declarados

// Escucha del Buscador de Clientes (Filtra de manera óptima por Nombre o por DNI)
        searchCustomerLoanField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLoanCustomers.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().trim();

                boolean matchName = user.getFirstName() != null && user.getFirstName().toLowerCase().contains(lowerCaseFilter);
                boolean matchDni = user.getDni() != null && user.getDni().toLowerCase().contains(lowerCaseFilter);

                return matchName || matchDni;
            });
        });

// C. Tabla Principal de Préstamos Activos (Derecha)
        loanTitleColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBook().getTitle())
        );
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

// Columna de Acción con el botón "Devolver"
        actionColumn.setCellFactory(param -> new TableCell<Loan, Void>() {
            private final Button returnButton = new Button("Devolver");
            {
                returnButton.setStyle("-fx-background-color: #5cb85c; -fx-text-fill: white; -fx-cursor: hand;");
                returnButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    if (loan != null) {
                        loanService.returnBook(loan);
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Libro Devuelto", "El inventario ha sido actualizado.");
                        refreshLoans();
                        refreshBooks();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(returnButton);
            }
        });

        // Mapea el método getStatus() del objeto Loan a la columna de la interfaz
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Opcional: Darle color al texto según el estado (Verde para Activo/Devuelto, Rojo para Vencido)
        stateColumn.setCellFactory(column -> new TableCell<Loan, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals("Vencido")) {
                        setStyle("-fx-text-fill: #d9534f; -fx-font-weight: bold;"); // Rojo
                    } else if (item.equals("Devuelto")) {
                        setStyle("-fx-text-fill: #5cb85c; -fx-font-weight: bold;"); // Verde
                    } else {
                        setStyle("-fx-text-fill: #0275d8; -fx-font-weight: bold;"); // Azul para Activo
                    }
                }
            }
        });

        loanTableView.setItems(loanList);
        refreshLoans();

        // === ACTUALIZACIÓN AUTOMÁTICA DEL HISTORIAL DE PRÉSTAMOS DEL CLIENTE ===
        customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldCustomer, newCustomer) -> {
            if (newCustomer != null) {
                // 1. Buscamos los préstamos del cliente seleccionado utilizando su ID o DNI
                List<Loan> customerLoans = loanService.findLoansByUserId(newCustomer.getId());

                // Nota: Si tu servicio filtra por DNI en lugar de ID, usa:
                // List<Loan> customerLoans = loanService.getLoansByUserDni(newCustomer.getDni());

                // 2. Limpiamos la lista vieja de la tabla pequeña y cargamos los nuevos registros
                ObservableList<Loan> customerLoanList = FXCollections.observableArrayList(customerLoans);
                loanTbleView1.setItems(customerLoanList);
            } else {
                // Si no hay ningún cliente seleccionado, vaciamos la tabla de préstamos
                loanTbleView1.getItems().clear();
            }
        });

        // === CONFIGURACIÓN DE COLUMNAS DE LA TABLA HISTORIAL CLIENTE ===
        loanTitleColumn1.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBook().getTitle())
        );
        loanDateColumn1.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        dueDateColumn1.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Formatear las fechas para que se vean en formato dd/MM/yyyy
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        loanDateColumn1.setCellFactory(col -> new TableCell<Loan, LocalDate>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(dateFormatter));
            }
        });

        dueDateColumn1.setCellFactory(col -> new TableCell<Loan, LocalDate>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(dateFormatter));
            }
        });


        // Detector de doble clic para editar o eliminar un libro
        bookTableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Book selectedBook = row.getItem();
                    openEditDialog("/fxml/book-edit-view.fxml", "Modificar información del Libro", selectedBook);
                }
            });
            return row;
        });

        // Detector de doble clic para editar o eliminar un cliente
        customerTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    User selectedCustomer = row.getItem();
                    openEditDialog("/fxml/customer-edit-view.fxml", "Detalles del Cliente", selectedCustomer);
                }
            });
            return row;
        });

        // Detector de doble clic para editar o eliminar un empleado
        employeeTableView.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>(); // Reemplaza Object por tu clase (ej. Employee o User)
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Object selectedEmployee = row.getItem();
                    openEditDialog("/fxml/employee-edit-view.fxml", "Perfil del Empleado", selectedEmployee);
                }
            });
            return row;
        });


        // === CONFIGURACIÓN PESTAÑA 4: GESTIÓN DE EMPLEADOS ===
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeUserColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        employeeActionColumn.setCellFactory(param -> new TableCell<Employee, Void>() {
            private final Button deleteButton = new Button("Eliminar");
            {
                deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-cursor: hand;");
                deleteButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    if (employee != null) {
                        employeeService.delete(employee.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Empleado eliminado", "El empleado ha sido dado de baja.");
                        refreshEmployees();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Vincular la lista de control observable a la vista de la tabla
        employeeTableView.setItems(employeeList);
        refreshEmployees();


        // Detector de doble clic para editar, devolver o eliminar un préstamo activo
        loanTableView.setRowFactory(tv -> {
            TableRow<Loan> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Loan selectedLoan = row.getItem();
                    openEditDialog("/fxml/loan-edit-view.fxml", "Gestión del Préstamo Activo", selectedLoan);
                }
            });
            return row;
        });

        rolComboBox.setPromptText("ROL");
        rolComboBox.setItems(javafx.collections.FXCollections.observableArrayList(
                "ADMINISTRADOR",
                "EMPLEADO",
                "BIBLIOTECARIO"
        ));
    }

    private void refreshEmployees() {
        List<Employee> employees = employeeService.findAll();
        employeeList.clear();
        employeeList.addAll(employees);
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

        categoryListView.setItems(FXCollections.observableArrayList(categories));
    }

    public void refreshBooks(){
        List<Book> books = bookService.findAll();
        bookList.clear();
        bookList.addAll(books);
    }

    @FXML
    public void clearFields(){

        isbnField.clear();
        titleField.clear();
        authorField.clear();
        categoryComboBox.setValue(null);
        stockField.clear();
        yearField.setValue(null);
        newCategoryField.clear();

    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleSaveCustomer() {
        String name = customerNameField.getText().trim();
        String firstAp = customerFirstNameField.getText().trim();
        String secondAp = customerSecondNameField.getText().trim();
        String dni = customerDniField.getText().trim();
        String email = customerEmailField.getText().trim();
        String phone = customerPhoneField.getText().trim();

        if (name.isEmpty() || firstAp.isEmpty() || dni.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Faltan datos obligatorios",
                    "Por favor, rellena al menos el Nombre, Apellidos, DNI y Correo.");
            return;
        }


        String apellidosCompletos = firstAp + (secondAp.isEmpty() ? "" : " " + secondAp);

        User user = new User(name, apellidosCompletos, dni, email, phone, java.time.LocalDate.now());

        try {

            userService.save(user);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente guardado", "El cliente se ha registrado correctamente.");
            handleClearCustomerFields();
            refreshCustomers();

        } catch (IllegalStateException e) {

            showAlert(Alert.AlertType.ERROR, "DNI Duplicado", "Error al guardar el cliente", e.getMessage());
        }

        handleClearCustomerFields();
        refreshCustomers();
    }

    @FXML
    private void handleClearCustomerFields() {
        customerNameField.clear();
        customerFirstNameField.clear();
        customerSecondNameField.clear();
        customerDniField.clear();
        customerEmailField.clear();
        customerPhoneField.clear();
    }

    private void refreshCustomers() {
        List<User> users = userService.findAll();
        customerList.clear();
        customerList.addAll(users);
    }

    @FXML
    private void registerNewLoan(ActionEvent event) {
        // 1. Capturamos el libro y el cliente seleccionados directamente de las filas de las tablas
        Book selectedBook = loanBookTableView.getSelectionModel().getSelectedItem();
        User selectedUser = loanCustomerTable.getSelectionModel().getSelectedItem();

        // 2. Validación: Comprobar que el usuario ha hecho clic y seleccionado ambos elementos
        if (selectedBook == null || selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "Selección incompleta", "Faltan datos por seleccionar",
                    "Por favor, selecciona un libro de la tabla superior y un cliente de la tabla inferior para realizar el préstamo.");
            return;
        }

        try {
            // 3. Procesamos el registro a través del servicio (descuenta stock y persiste el préstamo)
            loanService.registerLoan(selectedBook, selectedUser);

            // 4. Mostramos el mensaje de éxito
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo completado",
                    "El libro '" + selectedBook.getTitle() + "' ha sido asignado correctamente a " + selectedUser.getFirstName() + ".");

            // 5. Limpiamos visualmente la selección de las tablas para dejar la interfaz lista
            loanBookTableView.getSelectionModel().clearSelection();
            loanCustomerTable.getSelectionModel().clearSelection();

            // 6. Refrescamos todas las tablas de la aplicación para actualizar el stock e historiales
            refreshLoans();
            refreshBooks();
            refreshCustomers();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar el préstamo", e.getMessage());
        }
    }

    private void refreshLoans() {
        List<Loan> activeLoans = loanService.getActiveLoans();
        loanList.clear();
        loanList.addAll(activeLoans);
    }


    private void openEditDialog(String fxmlPath, String title, Object selectedItem) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));
            javafx.scene.Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof BookEditViewController && selectedItem instanceof Book) {
                ((BookEditViewController) controller).setBook((Book) selectedItem);
            }else if (controller instanceof CustomerEditViewController && selectedItem instanceof User) {
                ((CustomerEditViewController) controller).setCustomer((User) selectedItem);
            }else if (controller instanceof EmployeeEditViewController) {
                // Suponiendo que tu modelo de empleado se llama Employee (o User si es común)
                ((EmployeeEditViewController) controller).setEmployee(selectedItem);
            }else if (controller instanceof LoanEditViewController && selectedItem instanceof Loan) {
                // Inyectamos el préstamo seleccionado en el controlador de edición de préstamos
                ((LoanEditViewController) controller).setLoan((Loan) selectedItem);
            }

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle(title);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

            // Al cerrar la ventana modal, refrescamos la tabla principal por si hubo cambios
            refreshBooks();
            refreshBooks();
            refreshCustomers();
            refreshLoans();
            if (this.employeeTableView != null) {
                refreshEmployees();
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo abrir la ventana", e.getMessage());
        }
    }

    @FXML
    private void clearEmployeeFields(ActionEvent event) {
        userEmployeeField.clear();
        passwdEmployeeField.clear();
        rolComboBox.setValue(null);
        rolComboBox.setPromptText("ROL");
    }

    @FXML
    private void saveEmployee(ActionEvent event) {
        String username = userEmployeeField.getText().trim();
        String password = passwdEmployeeField.getText().trim();
        String role = rolComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Faltan datos", "Todos los campos son obligatorios para el registro.");
            return;
        }

        try {
            com.franRR.librarymanager.model.Employee newEmployee = new com.franRR.librarymanager.model.Employee();
            newEmployee.setUsername(username);
            newEmployee.setPassword(password);
            newEmployee.setRole(role);

            // Persistimos el nuevo usuario en Hibernate
            employeeService.save(newEmployee);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Registro completado", "El empleado ha sido registrado con éxito.");
            clearEmployeeFields(event);
            refreshEmployees();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar", e.getMessage());
        }
    }

}
