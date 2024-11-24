/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import modelo.DatabaseManager;
import view.ResultView;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controlador para la vista de resultados.
 */
public class ResultViewController {
    private ResultView view;
    private DatabaseManager dbManager;

    public ResultViewController(ResultView view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    /**
     * Ejecuta un query y llena la tabla de resultados en la vista.
     *
     * @param query El query SQL a ejecutar.
     */
    public void executeQuery(String query) {
        if (query == null || query.isEmpty()) {
            appendToTerminal("Por favor, proporciona un query válido.\n");
            return;
        }

        try {
            // Ejecutar el query
            ResultSet rs = dbManager.executeQuery(query);
            view.populateTable(rs); // Llenar la tabla de resultados en la vista
            appendToTerminal("Query ejecutado correctamente.\n");
        } catch (SQLException e) {
            appendToTerminal("Error ejecutando query: " + e.getMessage() + "\n");
        }
    }

    /**
     * Agrega texto a la terminal de la vista.
     *
     * @param message Mensaje a mostrar en la terminal.
     */
    private void appendToTerminal(String message) {
        TextArea terminal = view.getQueryTerminal();
        terminal.appendText(message);
    }
}
