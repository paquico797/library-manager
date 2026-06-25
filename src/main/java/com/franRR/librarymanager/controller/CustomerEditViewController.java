/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.franRR.librarymanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.franRR.librarymanager.model.User;
import com.franRR.librarymanager.services.UserService;
import com.franRR.librarymanager.services.impl.UserServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author paqui
 */
public class CustomerEditViewController implements Initializable {

    @FXML
    private TextField dniField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private Button saveChangesBtn;
    @FXML
    private Button eraseBtn;

    private final UserService userService = new UserServiceImpl();
    private User currentCustomer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setCustomer(User user) {
        this.currentCustomer = user;

        // Rellenamos los campos mapeados correctamente con las variables FXML existentes
        dniField.setText(user.getDni());
        nameField.setText(user.getFirstName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());

        // Mapeo inteligente para separar el lastName compuesto en primer y segundo apellido
        String completos = user.getLastName();
        if (completos != null && completos.contains(" ")) {
            firstNameField.setText(completos.split(" ")[0]);
            lastNameField.setText(completos.substring(completos.indexOf(" ") + 1));
        } else {
            firstNameField.setText(completos != null ? completos : "");
            lastNameField.setText("");
        }
    }

    @FXML
    private void saveCustomer(ActionEvent event) {

        String name = nameField.getText().trim();
        String firstAp = firstNameField.getText().trim();
        String secondAp = lastNameField.getText().trim();
        String dni = dniField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || firstAp.isEmpty() || dni.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Faltan datos obligatorios",
                    "Por favor, rellene al menos el Nombre, Primer Apellido, DNI y Correo.");
            return;
        }

        try {
            // Unificamos los apellidos antes de guardar
            String apellidosCompletos = firstAp + (secondAp.isEmpty() ? "" : " " + secondAp);

            // Actualizamos la entidad controlada
            currentCustomer.setDni(dni);
            currentCustomer.setFirstName(name);
            currentCustomer.setLastName(apellidosCompletos);
            currentCustomer.setEmail(email);
            currentCustomer.setPhone(phone);

            // Guardamos los cambios utilizando el servicio
            userService.save(currentCustomer);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente actualizado", "La información del cliente se guardó correctamente.");
            closeWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error al guardar", "No se pudieron aplicar los cambios", e.getMessage());
        }
    }

    @FXML
    private void eraseCustomer(ActionEvent event) {

        if (currentCustomer != null) {
            try {
                // Borramos el registro usando el identificador único (ID)
                userService.delete(currentCustomer.getId());
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente dado de baja", "El cliente ha sido eliminado del sistema.");
                closeWindow();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error de integridad", "No se puede eliminar",
                        "Asegúrese de que el cliente no tenga préstamos activos o pendientes de devolución vinculados.");
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
