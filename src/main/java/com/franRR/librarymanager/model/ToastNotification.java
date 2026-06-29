package com.franRR.librarymanager.model;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ToastNotification {

    public enum ToastType {
        SUCCESS, ERROR, WARNING
    }

    public static void show(Stage ownerStage, String message, ToastType type) {
        Popup popup = new Popup();

        // 1. Crear el diseño del mensaje flotante
        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(300);

        // Aplicar estilos modernos directamente o mediante clases CSS si lo prefieres
        label.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif; -fx-font-size: 13px; -fx-font-weight: bold;");

        VBox toastRoot = new VBox(label);
        toastRoot.setAlignment(Pos.CENTER);
        toastRoot.setPadding(new Insets(12, 24, 12, 24));

        // Configurar bordes redondeados y sombra sutil
        String baseStyle = "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);";

        // 2. Asignar colores planos y elegantes según el tipo de mensaje
        switch (type) {
            case SUCCESS:
                toastRoot.setStyle(baseStyle + " -fx-background-color: #e0f2fe;"); // Azul ártico/éxito suave
                label.setStyle(label.getStyle() + " -fx-text-fill: #0369a1;");
                break;
            case ERROR:
                toastRoot.setStyle(baseStyle + " -fx-background-color: #fee2e2;"); // Rojo/error suave
                label.setStyle(label.getStyle() + " -fx-text-fill: #b91c1c;");
                break;
            case WARNING:
                toastRoot.setStyle(baseStyle + " -fx-background-color: #fef3c7;"); // Amarillo/advertencia suave
                label.setStyle(label.getStyle() + " -fx-text-fill: #b45309;");
                break;
        }

        popup.getContent().add(toastRoot);

        // 3. Posicionar el Toast en la esquina inferior derecha del Stage actual
        popup.setOnShown(e -> {
            popup.setX(ownerStage.getX() + ownerStage.getWidth() - popup.getWidth() - 40);
            popup.setY(ownerStage.getY() + ownerStage.getHeight() - popup.getHeight() - 40);
        });

        popup.show(ownerStage);

        // 4. ANIMACIÓN: Aparece completo, se mantiene y se desvanece solo
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), toastRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        // Esperamos 2.5 segundos visible antes de empezar a desaparecer
        fadeOut.setDelay(Duration.millis(2500));

        // Cuando termine la animación de desaparecer, cerramos el popup de memoria
        fadeOut.setOnFinished(e -> popup.hide());
        fadeOut.play();
    }
}