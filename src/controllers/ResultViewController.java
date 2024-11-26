package controllers;

import modelo.DatabaseManager;
import view.ResultView;

import javafx.scene.control.TextInputDialog;

import java.sql.SQLException;

/**
 * Controlador para la vista de resultados, con funcionalidad para crear vistas.
 */
public class ResultViewController {
    private ResultView view;
    private DatabaseManager dbManager;
    private String query; // Query base para la vista
    private String selectedDatabase; // Base de datos seleccionada

    public ResultViewController(ResultView view, DatabaseManager dbManager, String query, String selectedDatabase) {
        this.view = view;
        this.dbManager = dbManager;
        this.query = query;
        this.selectedDatabase = selectedDatabase;

        setupAddViewButton(); // Configurar el botón "Añadir Vista"
    }
 //metodo para crear vista sobre elresutado del query
    private void setupAddViewButton() {
        view.getAddViewButton().setOnAction(event -> {
            // Mostrar cuadro de diálogo para ingresar el nombre de la vista
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Crear Vista");
            dialog.setHeaderText("Nombre de la Vista");
            dialog.setContentText("Introduce el nombre de la vista:");

            String viewName = dialog.showAndWait().orElse(null);

            if (viewName == null || viewName.trim().isEmpty()) {
                appendToTerminal("El nombre de la vista no puede estar vacío.\n");
                return;
            }

            // Validar el query
            if (query == null || query.isEmpty()) {
                appendToTerminal("El query base para la vista está vacío.\n");
                return;
            }

            // Crear la vista en la base de datos
            String createViewSQL = "CREATE VIEW " + selectedDatabase + "." + viewName + " AS " + query;

            boolean success = dbManager.executeUpdate(createViewSQL);
            if (success) {
                appendToTerminal("Vista creada exitosamente como: " + viewName + "\n");
            } else {
                appendToTerminal("Error al crear la vista. Revisa el query.\n");
            }
        });
    }

    /**
     * Agrega un mensaje al terminal en la vista.
     */
    private void appendToTerminal(String message) {
        view.getQueryTerminal().appendText(message);
    }
}
