package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;

public class QueryPreviewView {
    private BorderPane layout;
    private TextArea queryPreviewArea;
    private ComboBox<String> joinTypeComboBox;
    private ComboBox<String> table1FieldsComboBox;
    private ComboBox<String> table2FieldsComboBox;
    private Button applyJoinButton;

    public QueryPreviewView(List<String> selectedTables, String queryPreview) {
        layout = new BorderPane();
        layout.setPadding(new Insets(30));

        // Título
        Label titleLabel = new Label("Vista previa del Query");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox topBox = new VBox(titleLabel);
        topBox.setAlignment(Pos.CENTER);
        layout.setTop(topBox);

        // Mostrar tablas seleccionadas
        Label tablesLabel = new Label("Tablas seleccionadas:");
        tablesLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox tablesBox = new VBox(5, tablesLabel);
        for (String table : selectedTables) {
            Label tableLabel = new Label("- " + table);
            tablesBox.getChildren().add(tableLabel);
        }
        tablesBox.setPadding(new Insets(10));
        layout.setLeft(tablesBox);

        // Área de texto para la vista previa del query
        queryPreviewArea = new TextArea(queryPreview);
        queryPreviewArea.setEditable(false);
        queryPreviewArea.setWrapText(true);
        queryPreviewArea.setPrefHeight(200);
        queryPreviewArea.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; " +
                "-fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px;");

        // Configuración de opciones de JOIN
        Label joinOptionsLabel = new Label("Opciones de Join:");
        joinTypeComboBox = new ComboBox<>();
        joinTypeComboBox.getItems().addAll("INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN");

        // ComboBoxes para seleccionar campos de las tablas
        table1FieldsComboBox = new ComboBox<>();
        table2FieldsComboBox = new ComboBox<>();
        
        applyJoinButton = new Button("Aplicar JOIN");

        HBox joinOptionsBox = new HBox(10, joinTypeComboBox, table1FieldsComboBox, new Label("="), table2FieldsComboBox, applyJoinButton);
        joinOptionsBox.setAlignment(Pos.CENTER);
        joinOptionsBox.setPadding(new Insets(10));

        VBox mainBox = new VBox(10, joinOptionsLabel, joinOptionsBox, queryPreviewArea);
        mainBox.setPadding(new Insets(20));
        layout.setCenter(mainBox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public ComboBox<String> getJoinTypeComboBox() {
        return joinTypeComboBox;
    }

    public ComboBox<String> getTable1FieldsComboBox() {
        return table1FieldsComboBox;
    }

    public ComboBox<String> getTable2FieldsComboBox() {
        return table2FieldsComboBox;
    }

    public Button getApplyJoinButton() {
        return applyJoinButton;
    }

    public TextArea getQueryPreviewArea() {
        return queryPreviewArea;
    }
}
