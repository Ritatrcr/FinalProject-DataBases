package controllers;

import finalproject.FinalProject;
import modelo.DatabaseManager;
import view.ColumnSelectionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;

public class ColumnSlectionController {
    private ColumnSelectionView view;
    private DatabaseManager dbManager;
    private Map<String, Map<String, String>> selectedColumnsWithAliases;

    public ColumnSlectionController(ColumnSelectionView view) {
        this.view = view;
        this.selectedColumnsWithAliases = new HashMap<>();
        this.dbManager = DatabaseManager.getInstance();
    }

    public void setup(String[] selectedTables) {
        VBox tablesBox = view.getTablesBox();

        // Cargar columnas de cada tabla seleccionada
        for (String table : selectedTables) {
            VBox tableBox = new VBox();
            tableBox.setSpacing(5);

            // Título de la tabla
            Label tableLabel = new Label("Tabla: " + table);
            tableBox.getChildren().add(tableLabel);

            try {
                ResultSet columns = dbManager.getColumns(table); // Método para obtener columnas de la tabla
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");

                    // Checkbox para seleccionar la columna y campo para el alias
                    CheckBox columnCheckBox = new CheckBox(columnName);
                    TextField aliasField = new TextField();
                    aliasField.setPromptText("Alias");

                    HBox columnRow = new HBox(10, columnCheckBox, aliasField);
                    tableBox.getChildren().add(columnRow);

                    // Agregar evento para capturar selección y alias
                    columnCheckBox.setOnAction(event -> {
                        if (columnCheckBox.isSelected()) {
                            selectedColumnsWithAliases
                                .computeIfAbsent(table, k -> new HashMap<>())
                                .put(columnName, aliasField.getText());
                        } else {
                            selectedColumnsWithAliases.getOrDefault(table, new HashMap<>()).remove(columnName);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tablesBox.getChildren().add(tableBox);
        }

        view.getContinueButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Proceder con los datos seleccionados
            }
        });
    }
}
