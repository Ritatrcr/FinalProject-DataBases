package controllers;

import finalproject.FinalProject;
import modelo.DatabaseManager;
import view.FieldSelectionView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class FieldSelectionController {
    private FieldSelectionView view;
    private DatabaseManager dbManager;
    private Map<String, String> selectedFieldsWithAlias;

    public FieldSelectionController(FieldSelectionView view) {
        this.view = view;
        this.selectedFieldsWithAlias = new HashMap<>();
    }

    public void setup() {
        view.getConfirmButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedFieldsWithAlias.clear();
                
                // Recoger campos seleccionados y sus alias
                for (HBox fieldRow : view.getFieldRows()) {
                    CheckBox fieldCheckBox = (CheckBox) fieldRow.getChildren().get(0);
                    TextField aliasTextField = (TextField) fieldRow.getChildren().get(1);

                    if (fieldCheckBox.isSelected()) {
                        String fieldName = fieldCheckBox.getText();
                        String alias = aliasTextField.getText().isEmpty() ? fieldName : aliasTextField.getText();
                        selectedFieldsWithAlias.put(fieldName, alias);
                    }
                }

                // Lógica para ejecutar la consulta con los campos y alias seleccionados
                executeQueryWithSelectedFields();
            }
        });
    }

    private void executeQueryWithSelectedFields() {
        // Construir la consulta SQL usando los campos y alias seleccionados
        StringBuilder query = new StringBuilder("SELECT ");
        for (Map.Entry<String, String> entry : selectedFieldsWithAlias.entrySet()) {
            query.append(entry.getKey()).append(" AS ").append(entry.getValue()).append(", ");
        }
        query.setLength(query.length() - 2); // Eliminar la última coma
        query.append(" FROM ").append(FinalProject.getSelectedDatabase());

        // Ejecutar la consulta y mostrar los resultados en la vista
        dbManager.executeCustomQuery(query.toString(), view.getTerminalOutput());
    }
}
