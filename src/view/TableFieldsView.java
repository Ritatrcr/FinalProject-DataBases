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

    public TableFieldsView(TextArea sharedQueryTerminal) {
    if (sharedQueryTerminal == null) {
        throw new IllegalArgumentException("La terminal compartida no puede ser nula.");
    }
    this.queryTerminal = sharedQueryTerminal; // Usar la terminal compartida
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

        VBox table1Box = new VBox(new Label("Campos de la Tabla 1:"), table1FieldsTableView);
        VBox table2Box = new VBox(new Label("Campos de la Tabla 2:"), table2FieldsTableView);

        table1Box.setMaxWidth(Double.MAX_VALUE);
        table2Box.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(table1Box, Priority.ALWAYS);
        HBox.setHgrow(table2Box, Priority.ALWAYS);

        // Inicialmente, solo agregar la primera tabla
        centerSection.getChildren().add(table1Box);

        // Listener para ajustar dinámicamente las tablas
        table2FieldsTableView.itemsProperty().addListener((obs, oldItems, newItems) -> {
            if (newItems != null && !newItems.isEmpty()) {
                // Si hay elementos en la segunda tabla, agregarla y dividir espacio
                if (!centerSection.getChildren().contains(table2Box)) {
                    centerSection.getChildren().add(table2Box);
                }
                // Configurar tamaños divididos
                table1Box.setPrefWidth(0.5 * layout.getWidth());
                table2Box.setPrefWidth(0.5 * layout.getWidth());

                // Ajustar ancho de la columna Alias
                adjustAliasColumnWidth(table1FieldsTableView, 100); // Más pequeño
                adjustAliasColumnWidth(table2FieldsTableView, 100); // Más pequeño
            } else {
                // Si no hay elementos en la segunda tabla, ocupar todo el ancho con la primera tabla
                centerSection.getChildren().remove(table2Box);
                table1Box.setPrefWidth(layout.getWidth());

                // Ajustar ancho de la columna Alias
                adjustAliasColumnWidth(table1FieldsTableView, 150); // Más grande
            }
        });

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

    // Listener para construir el WHERE al seleccionar un campo
    selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(param -> {
        FieldAlias fieldAlias = tableView.getItems().get(param);
        fieldAlias.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (isTable1) {
                    selectedFieldTable1 = fieldAlias.getFieldName();
                } else {
                    selectedFieldTable2 = fieldAlias.getFieldName();
                }
            } else {
                if (isTable1) {
                    selectedFieldTable1 = null;
                } else {
                    selectedFieldTable2 = null;
                }
            }
            updateQueryTerminal();
        });
        return fieldAlias.selectedProperty();
    }));

    return tableView;
}


   private VBox createBottomSection() {
    VBox bottomSection = new VBox(15);
    bottomSection.setPadding(new Insets(10));

    if (queryTerminal == null) {
        throw new IllegalStateException("La terminal compartida no está inicializada.");
    }

    goBackButton = new Button("Volver a Configuración");

    bottomSection.getChildren().addAll(new Label("Query MySQL:"), queryTerminal, goBackButton);
    return bottomSection;
}



    private void adjustAliasColumnWidth(TableView<FieldAlias> tableView, double newWidth) {
        for (TableColumn<FieldAlias, ?> column : tableView.getColumns()) {
            if ("Alias".equals(column.getText())) {
                column.setPrefWidth(newWidth);
            }
        }
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

    private void updateQueryTerminal() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("SELECT ");

    boolean hasSelectedFields = false;

    // Obtener los campos seleccionados de la Tabla 1
    for (FieldAlias fieldAlias : table1FieldsTableView.getItems()) {
        if (fieldAlias.isSelected()) {
            queryBuilder.append(fieldAlias.getFieldName()).append(", ");
            hasSelectedFields = true;
        }
    }

    // Obtener los campos seleccionados de la Tabla 2
    for (FieldAlias fieldAlias : table2FieldsTableView.getItems()) {
        if (fieldAlias.isSelected()) {
            queryBuilder.append(fieldAlias.getFieldName()).append(", ");
            hasSelectedFields = true;
        }
    }

    if (hasSelectedFields) {
        queryBuilder.setLength(queryBuilder.length() - 2); // Eliminar la última coma
    } else {
        queryBuilder.append("*");
    }

    queryBuilder.append(" FROM ");

    if (!table1FieldsTableView.getItems().isEmpty()) {
        String table1Name = table1FieldsTableView.getItems().get(0).getFieldName().split("\\.")[0];
        queryBuilder.append(databaseName).append(".").append(table1Name);
    }

    if (!table2FieldsTableView.getItems().isEmpty()) {
        String table2Name = table2FieldsTableView.getItems().get(0).getFieldName().split("\\.")[0];
        queryBuilder.append(", ").append(databaseName).append(".").append(table2Name);
    }

    if (selectedFieldTable1 != null && selectedFieldTable2 != null) {
        queryBuilder.append(" WHERE ").append(selectedFieldTable1).append(" = ").append(selectedFieldTable2);
    }

      queryTerminal.setText(queryBuilder.toString());
}


    public String buildQuery() {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT ");

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

        if (hasSelectedFields) {
            queryBuilder.setLength(queryBuilder.length() - 2);
        } else {
            queryBuilder.append("*");
        }

        queryBuilder.append(" FROM ");

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

        if (selectedFieldTable1 != null && selectedFieldTable2 != null) {
            queryBuilder.append(" WHERE ").append(selectedFieldTable1).append(" = ").append(selectedFieldTable2);
        }

        return queryBuilder.toString();
    }
}
