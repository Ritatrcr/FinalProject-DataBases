package controllers;

import finalproject.FinalProject;
import java.sql.ResultSet;
import modelo.DatabaseManager;
import view.DatabaseSelectionView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controlador para DatabaseSelectionView.
 */
public class SelectDatabaseController {
    private DatabaseSelectionView view;
    private DatabaseManager dbManager;

    public SelectDatabaseController(DatabaseSelectionView view) {
        this.view = view;
    }

    /**
     * Configura los eventos y lógica de la vista.
     */
    public void setup() {
        Button previousButton = view.getPreviousButton();
        Button nextButton = view.getNextButton();
        ObservableList<String> databases = FXCollections.observableArrayList();

        // Obtener la instancia existente de DatabaseManager
        dbManager = DatabaseManager.getInstance();

        // Cargar las bases de datos
        loadDatabases(databases);

        // Crear CheckBoxes para cada base de datos dentro de databasesBox
        for (String dbName : databases) {
            CheckBox checkBox = new CheckBox(dbName);
            checkBox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    long selectedCount = view.getDatabasesBox().getChildren().stream()
                            .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                            .count();
                    nextButton.setDisable(selectedCount == 0 || selectedCount > 1);
                }
            });
            view.getDatabasesBox().getChildren().add(checkBox);
        }

        // Evento para habilitar "Siguiente" al seleccionar una base de datos
        // Esto ya se maneja en los CheckBoxes

        // Acción del botón "Anterior"
        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FinalProject.showConnectionScene();
            }
        });

        // Acción del botón "Siguiente"
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selectedDatabase = null;
                for (javafx.scene.Node node : view.getDatabasesBox().getChildren()) {
                    if (node instanceof CheckBox) {
                        CheckBox cb = (CheckBox) node;
                        if (cb.isSelected()) {
                            selectedDatabase = cb.getText();
                            break;
                        }
                    }
                }
                if (selectedDatabase != null) {
                    view.getTerminalOutput().appendText("Base de datos seleccionada: " + selectedDatabase + "\n");
                    FinalProject.setSelectedDatabase(selectedDatabase);
                    FinalProject.showTableSelectionScene();
                }
            }
        });
    }

    /**
     * Carga las bases de datos disponibles en el ObservableList.
     */
    private void loadDatabases(ObservableList<String> databases) {
        try {
            if (dbManager.connect()) { // Asegurar conexión
                ResultSet rs = dbManager.getDatabases();
                while (rs.next()) {
                    databases.add(rs.getString(1));
                }
            } else {
                view.getTerminalOutput().appendText("No se pudo conectar a la base de datos para cargar las bases disponibles.\n");
            }
        } catch (Exception e) {
            view.getTerminalOutput().appendText("Error al cargar bases de datos: " + e.getMessage() + "\n");
        }
    }
}
