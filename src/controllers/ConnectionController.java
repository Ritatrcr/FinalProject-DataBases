package controllers;

import finalproject.FinalProject;
import modelo.DatabaseManager;
import view.ConnectionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Controlador para ConnectionView.
 */
public class ConnectionController {
    private ConnectionView view;
    private DatabaseManager dbManager;

    public ConnectionController(ConnectionView view) {
        this.view = view;
    }

    /**
     * Configura los eventos y lógica de la vista.
     */
    public void setup() {
        Button connectButton = view.getConnectButton();
        Button nextButton = view.getNextButton();

        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ip = view.getTextFieldIP().getText();
                String port = view.getTextFieldPort().getText();
                String username = view.getTextFieldUsername().getText();
                String password = view.getPasswordField().getText();

                // Validaciones básicas
                if (ip.isEmpty() || port.isEmpty() || username.isEmpty()) {
                    view.getTerminalOutput().appendText("Por favor, completa todos los campos.\n");
                    return;
                }

                // Inicializar DatabaseManager
                dbManager = DatabaseManager.getInstance(ip, port, username, password, view.getTerminalOutput());

                if (dbManager.connect()) {
                    nextButton.setDisable(false);
                } else {
                    nextButton.setDisable(true);
                }
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FinalProject.showDatabaseSelectionScene();
            }
        });
    }
}
