/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Vista para la selección inicial del usuario.
 */
public class UserSelectionView {
    private VBox layout;
    private Button consultarRegistrosButton;
    private Button realizarQueryButton;

    /**
     * Constructor para la vista de selección de usuario.
     */
    public UserSelectionView() {
        layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Etiqueta de bienvenida
        Label welcomeLabel = new Label("Bienvenido, selecciona una opción para comenzar");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Botones de selección
        consultarRegistrosButton = new Button("Consultar registros de una tabla");
        realizarQueryButton = new Button("Realizar Query");

        // Agregar componentes al layout
        layout.getChildren().addAll(welcomeLabel, consultarRegistrosButton, realizarQueryButton);
    }

    /**
     * Retorna el layout principal de la vista.
     *
     * @return VBox con el contenido.
     */
    public VBox getLayout() {
        return layout;
    }

    /**
     * Retorna el botón para consultar registros.
     *
     * @return Botón de consultar registros.
     */
    public Button getConsultarRegistrosButton() {
        return consultarRegistrosButton;
    }

    /**
     * Retorna el botón para realizar queries.
     *
     * @return Botón de realizar queries.
     */
    public Button getRealizarQueryButton() {
        return realizarQueryButton;
    }
}
