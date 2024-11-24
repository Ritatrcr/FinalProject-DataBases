package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Vista para mostrar y asignar alias a campos de las tablas seleccionadas.
 */
public class TableFieldsView {
    private BorderPane layout;
    private TableView<FieldAlias> table1FieldsTableView;
    private TableView<FieldAlias> table2FieldsTableView;
    private TextArea queryTerminal;
    private Button goBackButton;
    private String databaseName;
    // Mapas para almacenar los alias asignados a los campos
    private Map<String, String> table1FieldAliases = new HashMap<>();
    private Map<String, String> table2FieldAliases = new HashMap<>();

    // Campos seleccionados para relacionar
    private String selectedFieldTable1;
    private String selectedFieldTable2;
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public TableFieldsView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        layout.setCenter(centerSection);
        layout.setBottom(bottomSection);
    }

    private HBox createCenterSection() {
        HBox centerSection = new HBox(15);
        centerSection.setPadding(new Insets(10));

        // TableView para campos de la tabla 1
        table1FieldsTableView = createFieldsTableView(table1FieldAliases, true);

        // TableView para campos de la tabla 2
        table2FieldsTableView = createFieldsTableView(table2FieldAliases, false);

        centerSection.getChildren().addAll(
                new VBox(new Label("Campos de la Tabla 1:"), table1FieldsTableView),
                new VBox(new Label("Campos de la Tabla 2:"), table2FieldsTableView)
        );
        return centerSection;
    }

    private TableView<FieldAlias> createFieldsTableView(Map<String, String> fieldAliases, boolean isTable1) {
        TableView<FieldAlias> tableView = new TableView<>();

        TableColumn<FieldAlias, Boolean> selectColumn = new TableColumn<>("Seleccionar");
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        TableColumn<FieldAlias, String> fieldColumn = new TableColumn<>("Campo");
        fieldColumn.setCellValueFactory(cellData -> cellData.getValue().fieldNameProperty());
        fieldColumn.setEditable(false);

        TableColumn<FieldAlias, String> aliasColumn = new TableColumn<>("Alias");
        aliasColumn.setCellValueFactory(cellData -> cellData.getValue().aliasProperty());
        aliasColumn.setEditable(true);

        // Hacer la columna de alias editable
        aliasColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        aliasColumn.setOnEditCommit(event -> {
            FieldAlias fieldAlias = event.getRowValue();
            String newAlias = event.getNewValue();
            fieldAlias.setAlias(newAlias);
            fieldAliases.put(fieldAlias.getFieldName(), newAlias);
            updateQueryTerminal();
        });

        tableView.getColumns().addAll(selectColumn, fieldColumn, aliasColumn);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Permitir selección única para el campo de relación
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Agregar listener para manejar la selección de campos para la condición WHERE
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String selectedField = newSelection.getFieldName();
                if (isTable1) {
                    selectedFieldTable1 = selectedField;
                } else {
                    selectedFieldTable2 = selectedField;
                }
                updateQueryTerminal();
            } else {
                if (isTable1) {
                    selectedFieldTable1 = null;
                } else {
                    selectedFieldTable2 = null;
                }
                updateQueryTerminal();
            }
        });

        return tableView;
    }

    private VBox createBottomSection() {
        VBox bottomSection = new VBox(15);
        bottomSection.setPadding(new Insets(10));

        queryTerminal = new TextArea();
        queryTerminal.setEditable(false);
        queryTerminal.setPrefHeight(100);

        goBackButton = new Button("Volver a Configuración");

        bottomSection.getChildren().addAll(new Label("Query en construcción:"), queryTerminal, goBackButton);
        return bottomSection;
    }

    public BorderPane getLayout() {
        return layout;
    }

    public Button getGoBackButton() {
        return goBackButton;
    }

    public TableView<FieldAlias> getTable1FieldsTableView() {
        return table1FieldsTableView;
    }

    public TableView<FieldAlias> getTable2FieldsTableView() {
        return table2FieldsTableView;
    }

    public Map<String, String> getTable1FieldAliases() {
        return table1FieldAliases;
    }

    public Map<String, String> getTable2FieldAliases() {
        return table2FieldAliases;
    }

    public String getSelectedFieldTable1() {
        return selectedFieldTable1;
    }

    public String getSelectedFieldTable2() {
        return selectedFieldTable2;
    }

    public void populateTableFields(ObservableList<FieldAlias> table1Fields, ObservableList<FieldAlias> table2Fields) {
        table1FieldsTableView.setItems(table1Fields);
        table2FieldsTableView.setItems(table2Fields);

        // Agregar listeners para actualizar el query cuando cambia la selección
        for (FieldAlias fieldAlias : table1Fields) {
            fieldAlias.selectedProperty().addListener((obs, oldVal, newVal) -> updateQueryTerminal());
        }

        for (FieldAlias fieldAlias : table2Fields) {
            fieldAlias.selectedProperty().addListener((obs, oldVal, newVal) -> updateQueryTerminal());
        }
    }

    public TextArea getQueryTerminal() {
        return queryTerminal;
    }

    // Método para actualizar el query en el terminal
    private void updateQueryTerminal() {
        String query = buildQuery();
        queryTerminal.setText(query);
    }

    // Método para construir el query parcial basado en los campos seleccionados y alias
   public String buildQuery() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("SELECT ");

    // Añadir campos seleccionados de las tablas...
    boolean hasSelectedFields = false;
    for (FieldAlias fieldAlias : table1FieldsTableView.getItems()) {
        if (fieldAlias.isSelected()) {
            String fieldName = fieldAlias.getFieldName();
            String alias = fieldAlias.getAlias();
            if (!alias.isEmpty()) {
                queryBuilder.append(fieldName).append(" AS ").append(alias).append(", ");
            } else {
                queryBuilder.append(fieldName).append(", ");
            }
            hasSelectedFields = true;
        }
    }

    for (FieldAlias fieldAlias : table2FieldsTableView.getItems()) {
        if (fieldAlias.isSelected()) {
            String fieldName = fieldAlias.getFieldName();
            String alias = fieldAlias.getAlias();
            if (!alias.isEmpty()) {
                queryBuilder.append(fieldName).append(" AS ").append(alias).append(", ");
            } else {
                queryBuilder.append(fieldName).append(", ");
            }
            hasSelectedFields = true;
        }
    }

    // Si no hay campos seleccionados, usar *
    if (hasSelectedFields) {
        // Eliminar la última coma y espacio
        queryBuilder.setLength(queryBuilder.length() - 2);
    } else {
        queryBuilder.append("*");
    }

    // FROM clause
    queryBuilder.append(" FROM ");

    // Obtener los nombres de las tablas incluyendo el nombre de la base de datos
    String table1Name = null;
    String table2Name = null;

    if (!table1FieldsTableView.getItems().isEmpty()) {
        String fieldName = table1FieldsTableView.getItems().get(0).getFieldName();
        table1Name = fieldName.split("\\.")[0];
        queryBuilder.append(databaseName).append(".").append(table1Name);
    }

    if (!table2FieldsTableView.getItems().isEmpty()) {
        String fieldName = table2FieldsTableView.getItems().get(0).getFieldName();
        table2Name = fieldName.split("\\.")[0];
        if (table1Name != null) {
            queryBuilder.append(", ").append(databaseName).append(".").append(table2Name);
        } else {
            queryBuilder.append(databaseName).append(".").append(table2Name);
        }
    }

    // WHERE clause para relacionar campos seleccionados
    if (selectedFieldTable1 != null && selectedFieldTable2 != null) {
        queryBuilder.append(" WHERE ").append(selectedFieldTable1).append(" = ").append(selectedFieldTable2);
    }

    return queryBuilder.toString();
}


}
