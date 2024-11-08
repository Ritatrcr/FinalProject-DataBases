package controllers;

import finalproject.FinalProject;
import view.QueryPreviewView;
import modelo.DatabaseManager;
import view.ColumnSelectionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ColumnSlectionController {
    private ColumnSelectionView view;
    private DatabaseManager dbManager;

    public ColumnSlectionController(ColumnSelectionView view) {
        this.view = view;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void setup(String[] selectedTables) {
        for (String table : selectedTables) {
            try {
                view.addTableColumns(table, dbManager.getColumnsDetailsAsObservableList(table));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Acción para el botón "Anterior"
        view.getPreviousButton().setOnAction(event -> FinalProject.showTableSelectionScene());

        // Acción para el botón "Continuar"
        view.getContinueButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Obtener las columnas seleccionadas y sus alias
                Map<String, Map<String, String>> selectedColumnsWithAliases = view.getSelectedColumnsWithAliases();
                
                // Construir el query preview
                String queryPreview = view.updateQueryPreview();

                // Crear lista de tablas seleccionadas
                List<String> selectedTablesList = new ArrayList<>();
                for (String table : selectedTables) {
                    selectedTablesList.add(table);
                }

                // Mostrar la nueva vista con tablas seleccionadas y query
                FinalProject.showQueryPreviewScene(selectedTablesList, queryPreview);
            }
        });
    }
}
