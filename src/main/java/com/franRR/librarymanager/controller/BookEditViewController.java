/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.franRR.librarymanager.controller;

import com.franRR.librarymanager.model.Book;
import com.franRR.librarymanager.model.Category;
import com.franRR.librarymanager.services.BookService;
import com.franRR.librarymanager.services.CategoryService;
import com.franRR.librarymanager.services.impl.BookServiceImpl;
import com.franRR.librarymanager.services.impl.CategoryServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author paqui
 */
public class BookEditViewController implements Initializable {

    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private DatePicker publicationDateField;
    @FXML
    private TextField stockField;
    @FXML
    private Button btnSave;

    private final BookService bookService = new BookServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    private Book currentBook;
    @FXML
    private Button btnErase;
    @FXML
    private TextField authorField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        categoryComboBox.setItems(FXCollections.observableArrayList(categoryService.findAll()));
    }

    public void setBook(Book book) {
        this.currentBook = book;

        // Rellenamos los controles visuales con los datos actuales de la entidad
        isbnField.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        publicationDateField.setValue(book.getPublicationDate());
        stockField.setText(String.valueOf(book.getAvailableQuantity()));

        // Seleccionamos la categoría correcta en el ComboBox
        if (book.getCategory() != null) {
            for (Category cat : categoryComboBox.getItems()) {
                if (cat.getId().equals(book.getCategory().getId())) {
                    categoryComboBox.setValue(cat);
                    break;
                }
            }
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) {

        if (isbnField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() ||
                categoryComboBox.getValue() == null || publicationDateField.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "No se puede actualizar", "Rellene todos los campos obligatorios.");
            return;
        }

        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                showAlert(Alert.AlertType.ERROR, "Stock inválido", "Error de cantidad", "La cantidad en stock no puede ser menor a 0.");
                return;
            }

            // Actualizamos el objeto de Hibernate
            currentBook.setIsbn(isbnField.getText().trim());
            currentBook.setTitle(titleField.getText().trim());
            currentBook.setCategory(categoryComboBox.getValue());
            currentBook.setPublicationDate(publicationDateField.getValue());
            currentBook.setAvailableQuantity(stock);

            // Persistimos los cambios en la base de datos
            bookService.update(currentBook);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Libro actualizado", "Los cambios se guardaron correctamente.");
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de formato", "Stock inválido", "La cantidad en stock debe ser un número entero válido.");
        }
    }

    @FXML
    private void eraseBook(ActionEvent event) {

        if (currentBook != null) {
            try {
                // Borramos la entidad del ecosistema usando el servicio
                bookService.delete(currentBook);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Libro eliminado", "El libro ha sido removido del catálogo.");
                closeWindow();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo borrar", "Verifique que el libro no esté asociado a préstamos activos.");
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



    
}
