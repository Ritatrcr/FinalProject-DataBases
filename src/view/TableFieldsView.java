package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import modelo.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

public class TableFieldsView {
    private BorderPane layout;
    private ListView<String> fieldsListView;
    private TextArea queryTerminal;
    private Button goBackButton;

    public TableFieldsView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        VBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        layout.setCenter(centerSection);
        layout.setBottom(bottomSection);
    }

    private VBox createCenterSection() {
        VBox centerSection = new VBox(15);
        centerSection.setPadding(new Insets(10));

        fieldsListView = new ListView<>();
        fieldsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        centerSection.getChildren().addAll(new Label("Campos de las tablas seleccionadas:"), fieldsListView);
        return centerSection;
    }

    private VBox createBottomSection() {
        VBox bottomSection = new VBox(15);
        bottomSection.setPadding(new Insets(10));

        queryTerminal = new TextArea();
        queryTerminal.setEditable(false);
        queryTerminal.setPrefHeight(100);

        goBackButton = new Button("Volver a Configuración");

        bottomSection.getChildren().addAll(new Label("Query MySQL:"), queryTerminal, goBackButton);
        return bottomSection;
    }

    public BorderPane getLayout() {
        return layout;
    }

    public Button getGoBackButton() {
        return goBackButton;
    }

    public void populateTableFields(String selectedDatabase, String table1, String table2, DatabaseManager dbManager) throws SQLException {
        fieldsListView.getItems().clear();

        if (table1 != null && !table1.isEmpty()) {
            List<String> columnsTable1 = dbManager.getColumnNames(selectedDatabase, table1);
            for (String column : columnsTable1) {
                fieldsListView.getItems().add(table1 + "." + column);
            }
        }

        if (table2 != null && !table2.isEmpty()) {
            List<String> columnsTable2 = dbManager.getColumnNames(selectedDatabase, table2);
            for (String column : columnsTable2) {
                fieldsListView.getItems().add(table2 + "." + column);
            }
        }
    }
}
