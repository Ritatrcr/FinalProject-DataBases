package controllers;

import finalproject.FinalProject;
import view.QueryPreviewView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.List;
import java.util.Arrays;

public class QueryPreviewViewController {
    private QueryPreviewView view;
    private String queryPreview;
    private List<String> selectedTables;

    public QueryPreviewViewController(QueryPreviewView view, List<String> selectedTables, String initialQuery) {
        this.view = view;
        this.selectedTables = selectedTables;
        this.queryPreview = initialQuery;
        setup();
    }

    private void setup() {
        // Inicializar ComboBoxes de campos con campos de las tablas seleccionadas
        if (selectedTables.size() >= 2) {
            // Asumimos que obtenemos columnas de alguna fuente (simulando)
            List<String> table1Fields = Arrays.asList("id", "name", "date");  // Campos de la tabla 1
            List<String> table2Fields = Arrays.asList("id", "name", "value"); // Campos de la tabla 2

            view.getTable1FieldsComboBox().getItems().addAll(table1Fields);
            view.getTable2FieldsComboBox().getItems().addAll(table2Fields);
        }

        // Acción para el botón "Aplicar JOIN"
        view.getApplyJoinButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                applyJoin();
            }
        });

        // Mostrar el query inicial en el área de vista previa
        view.getQueryPreviewArea().setText(queryPreview);
    }

    private void applyJoin() {
        String joinType = view.getJoinTypeComboBox().getValue();
        String table1Field = view.getTable1FieldsComboBox().getValue();
        String table2Field = view.getTable2FieldsComboBox().getValue();

        if (joinType != null && table1Field != null && table2Field != null) {
            queryPreview += " " + joinType + " ON " + selectedTables.get(0) + "." + table1Field + " = " + selectedTables.get(1) + "." + table2Field;
            view.getQueryPreviewArea().setText(queryPreview);
        } else {
            // Mostrar mensaje de error si alguna opción de JOIN no se ha seleccionado
            System.out.println("Por favor, seleccione el tipo de JOIN y los campos para la condición.");
        }
    }
}
