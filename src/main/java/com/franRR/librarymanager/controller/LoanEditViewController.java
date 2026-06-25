package com.franRR.librarymanager.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import com.franRR.librarymanager.model.Loan;
import com.franRR.librarymanager.services.LoanService;
import com.franRR.librarymanager.services.impl.LoanServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author paqui
 */
public class LoanEditViewController implements Initializable {

    @FXML
    private TextField bookNameField;
    @FXML
    private TextField customerNameField;
    @FXML
    private DatePicker loanSrartDate;
    @FXML
    private DatePicker loanLimitDate;
    @FXML
    private Label actualStateLabel;
    @FXML
    private Button saveChangesBtn;
    @FXML
    private Button eraseBtn;

    private final LoanService loanService = new LoanServiceImpl();
    private Loan currentLoan;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        bookNameField.setEditable(false);
        customerNameField.setEditable(false);
    }

    public void setLoan(Loan loan) {
        this.currentLoan = loan;

        // Rellenamos la información visual del préstamo
        if (loan.getBook() != null) {
            bookNameField.setText(loan.getBook().getTitle());
        }
        if (loan.getUser() != null) {
            customerNameField.setText(loan.getUser().getFirstName() + " " + loan.getUser().getLastName());
        }

        loanSrartDate.setValue(loan.getLoanDate());
        loanLimitDate.setValue(loan.getDueDate());

        // Mostramos el estado actual
        actualStateLabel.setText(loan.getStatus() != null ? loan.getStatus() : "Activo");
    }

    @FXML
    private void saveChanges(ActionEvent event) {

        if (loanSrartDate.getValue() == null || loanLimitDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Fechas incompletas", "Campos vacíos", "Las fechas de inicio y límite son obligatorias.");
            return;
        }

        if (loanLimitDate.getValue().isBefore(loanSrartDate.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Fechas inválidas", "Error de cronología", "La fecha límite no puede ser anterior a la fecha de inicio.");
            return;
        }

        try {
            // Actualizamos los rangos de tiempo del préstamo en la entidad
            currentLoan.setLoanDate(loanSrartDate.getValue());
            currentLoan.setDueDate(loanLimitDate.getValue());




            new com.franRR.librarymanager.repository.impl.LoanRepositoryHibernate().update(currentLoan);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo modificado", "Las fechas del préstamo han sido actualizadas correctamente.");
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error al guardar", "No se pudieron aplicar los cambios", e.getMessage());
        }
    }

    @FXML
    private void erase(ActionEvent event) {

        if (currentLoan != null) {
            try {
                // Aquí simulamos o procesamos la devolución del libro (reincorporando stock) o eliminación física
                // Si deseas borrarlo por completo:
                // loanService.delete(currentLoan.getId());

                // Si deseas usar la lógica de Devolución existente en tu servicio:
                loanService.returnBook(currentLoan);

                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Préstamo procesado", "El libro ha sido marcado como devuelto y el stock actualizado.");
                closeWindow();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo procesar la acción", e.getMessage());
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveChangesBtn.getScene().getWindow();
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
