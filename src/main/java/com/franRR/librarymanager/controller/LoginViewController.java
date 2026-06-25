/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.franRR.librarymanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.franRR.librarymanager.model.Employee;
import com.franRR.librarymanager.services.EmployeeService;
import com.franRR.librarymanager.services.impl.EmployeeServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author paqui
 */
public class LoginViewController implements Initializable {

    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwdUserField;
    @FXML
    private Text registerButton;
    @FXML
    private Button loginBtn;

    private final EmployeeService employeeService = new EmployeeServiceImpl();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void registerUser(MouseEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/employee-edit-view.fxml"));
            javafx.scene.Parent root = loader.load();

            EmployeeEditViewController controller = loader.getController();
            // Le indicamos explícitamente al controlador que active el modo registro
            controller.setRegisterMode();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Registrar Nuevo Empleado");
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

        } catch (java.io.IOException e) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana de registro");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void login(ActionEvent event) {
        String username = userNameField.getText().trim();
        String password = passwdUserField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Faltan datos", "Por favor, introduce tu usuario y contraseña.");
            return;
        }

        try {
            // Buscamos al empleado en la base de datos por su nombre de usuario
            Employee employee = employeeService.findByUsername(username);

            // Validamos que exista y que la contraseña coincida
            if (employee != null && employee.getPassword().equals(password)) {

                // 1. Cargamos el FXML de la ventana principal (MainView)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
                Parent root = loader.load();

                // 2. Creamos el nuevo escenario para el menú principal
                Stage mainStage = new Stage();
                mainStage.setTitle("Library Manager - Menú Principal");
                mainStage.setScene(new Scene(root));
                mainStage.show();

                // 3. Cerramos la ventana actual de Login de forma limpia
                Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                currentStage.close();

            } else {
                showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Credenciales incorrectas", "El usuario o la contraseña no son válidos.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error de Conexión", "No se pudo validar el usuario", e.getMessage());
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
