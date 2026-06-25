package com.franRR.librarymanager.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;

import com.franRR.librarymanager.model.Employee;
import com.franRR.librarymanager.services.EmployeeService;
import com.franRR.librarymanager.services.impl.EmployeeServiceImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author paqui
 */
public class EmployeeEditViewController implements Initializable {

    @FXML
    private TextField userField;
    @FXML
    private TextField passwdField;
    @FXML
    private ComboBox<String> rolComboBox;
    @FXML
    private Button btnAdd;
    @FXML
    private Button saveChangesBtn;
    @FXML
    private Button eraseButton;

    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private Employee currentEmployee;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        rolComboBox.setItems(FXCollections.observableArrayList("ADMINISTRADOR", "EMPLEADO", "BIBLIOTECARIO"));
    }

    public void setEmployee(Object employee) {
        if (employee instanceof Employee) {
            this.currentEmployee = (Employee) employee;

            // Rellenamos el formulario con los datos actuales
            userField.setText(currentEmployee.getUsername());
            passwdField.setText(""); // Por seguridad, la contraseña se deja vacía para decidir si cambiarla o no
            rolComboBox.setValue(currentEmployee.getRole());

            // Ocultamos el botón original de "Añadir" ya que estamos en modo edición
            if (btnAdd != null) {
                btnAdd.setVisible(false);
            }
        }
    }

    // Método para configurar la ventana en modo creación/registro
    public void setRegisterMode() {
        this.currentEmployee = null; // No hay un empleado cargado de la base de datos

        // Aseguramos que el botón de añadir sea visible y esté activo
        if (btnAdd != null) {
            btnAdd.setVisible(true);
            btnAdd.setDisable(false);
        }

        // Ocultamos y deshabilitamos por completo los botones de edición y borrado
        if (saveChangesBtn != null) {
            saveChangesBtn.setVisible(false);
            saveChangesBtn.setDisable(true);
        }
        if (eraseButton != null) {
            eraseButton.setVisible(false);
            eraseButton.setDisable(true);
        }

        // Limpiamos los campos por si acaso
        userField.setText("");
        passwdField.setText("");
        rolComboBox.setValue(null);
    }

    @FXML
    private void addUser(ActionEvent event) {
        String username = userField.getText().trim();
        String password = passwdField.getText().trim();
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
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) {

        String username = userField.getText().trim();
        String password = passwdField.getText().trim();
        String role = rolComboBox.getValue();

        if (username.isEmpty() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Faltan datos", "El nombre de usuario y el rol son campos obligatorios.");
            return;
        }

        try {
            // Actualizamos las propiedades de la entidad gestionada por Hibernate
            currentEmployee.setUsername(username);
            currentEmployee.setRole(role);

            // Si el campo de contraseña no está vacío, la actualizamos
            if (!password.isEmpty()) {
                currentEmployee.setPassword(password);
            }

            // Realizamos la persistencia del cambio mediante el servicio
            employeeService.save(currentEmployee);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Empleado actualizado", "Los cambios del perfil se guardaron correctamente.");
            closeWindow();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error al guardar", "No se pudo actualizar el perfil", e.getMessage());
        }
    }

    @FXML
    private void erase(ActionEvent event) {
        if (currentEmployee != null) {
            try {
                // Borramos el registro usando el ID único de la entidad
                employeeService.delete(currentEmployee.getId());
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Empleado eliminado", "El empleado ha sido dado de baja en el sistema.");
                closeWindow();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error de integridad", "No se puede eliminar", "Verifique las restricciones de clave foránea en la base de datos.");
            }
        }
    }

    private void closeWindow() {
        Stage stage = null;

        // Si el botón de guardar está activo, lo usamos para obtener la ventana
        if (saveChangesBtn != null && saveChangesBtn.getScene() != null) {
            stage = (Stage) saveChangesBtn.getScene().getWindow();
        }
        // Si no (modo registro), usamos el botón de añadir que sí está renderizado y activo
        else if (btnAdd != null && btnAdd.getScene() != null) {
            stage = (Stage) btnAdd.getScene().getWindow();
        }
        // Como último recurso, si el de borrar estuviera disponible
        else if (eraseButton != null && eraseButton.getScene() != null) {
            stage = (Stage) eraseButton.getScene().getWindow();
        }

        // Si encontramos la ventana, la cerramos
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
