package controllers;

import finalproject.FinalProject;
import modelo.DatabaseManager;
import view.TableSelectionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;

/**
 * Controlador para TableSelectionView.
 */
public class TableSelectionController {
    private TableSelectionView view;
    private DatabaseManager dbManager;
    private String selectedDatabase;

    public TableSelectionController(TableSelectionView view) {
        this.view = view;
    }

    /**
     * Configura los eventos y lógica de la vista.
     */
    public void setup() {
        Button previousButton = view.getPreviousButton();
        Button nextTableButton = view.getNextTableButton();
        VBox tablesBox = view.getTablesBox();

        // Obtener la instancia existente de DatabaseManager y la base de datos seleccionada
        dbManager = DatabaseManager.getInstance();
        selectedDatabase = FinalProject.getSelectedDatabase();

        // Cargar las tablas de la base de datos seleccionada
        loadTables(tablesBox, selectedDatabase);

        // Acción del botón "Anterior"
        previousButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FinalProject.showDatabaseSelectionScene();
            }
        });

        // Acción del botón "Siguiente"
        nextTableButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder selectedTables = new StringBuilder("Tablas seleccionadas:\n");
                for (javafx.scene.Node node : tablesBox.getChildren()) {
                    if (node instanceof CheckBox) {
                        CheckBox cb = (CheckBox) node;
                        if (cb.isSelected()) {
                            selectedTables.append("- ").append(cb.getText()).append("\n");
                        }
                    }
                }
                view.getTerminalOutput().appendText(selectedTables.toString());
                // Aquí puedes añadir lógica adicional para manejar las tablas seleccionadas.
            }
        });
    }

    /**
     * Carga las tablas de la base de datos seleccionada en el VBox.
     */
    private void loadTables(VBox tablesBox, String databaseName) {
        if (databaseName == null || databaseName.isEmpty()) {
            view.getTerminalOutput().appendText("No se ha seleccionado ninguna base de datos.\n");
            return;
        }

        try {
            if (dbManager.connect()) { // Asegurar conexión
                ResultSet rs = dbManager.getTables(databaseName);
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    CheckBox checkBox = new CheckBox(tableName);
                    checkBox.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            long selectedCount = tablesBox.getChildren().stream()
                                    .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                                    .count();
                            view.getNextTableButton().setDisable(selectedCount == 0 || selectedCount > 2);
                        }
                    });
                    tablesBox.getChildren().add(checkBox);
                }
            } else {
                view.getTerminalOutput().appendText("No se pudo conectar a la base de datos para cargar las tablas.\n");
            }
        } catch (Exception e) {
            view.getTerminalOutput().appendText("Error al cargar tablas: " + e.getMessage() + "\n");
        }
    }
}
