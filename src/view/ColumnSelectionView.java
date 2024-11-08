package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import modelo.ColumnDetails;

import java.util.HashMap;
import java.util.Map;

public class ColumnSelectionView {
    private BorderPane layout;
    private VBox tablesBox;
    private Button continueButton;
    private Button previousButton;
    private TextArea queryPreviewTerminal; // Área de texto para simular la terminal
    private Map<String, Map<String, String>> selectedColumnsWithAliases; // Almacenar columnas seleccionadas con alias

    public ColumnSelectionView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(30));
        selectedColumnsWithAliases = new HashMap<>();

        // Instrucción para el usuario
        Label instructionLabel = new Label("Seleccione columnas y asigne un alias:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox topBox = new HBox(instructionLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        layout.setTop(topBox);

        // Caja central para tablas y columnas
        tablesBox = new VBox(10);
        tablesBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tablesBox);
        scrollPane.setFitToWidth(true);
        layout.setCenter(scrollPane);

        // Botones de "Anterior" y "Continuar"
        previousButton = new Button("Anterior");
        continueButton = new Button("Continuar");

        HBox bottomBox = new HBox(10, previousButton, continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));

        // Área de texto para mostrar el query en construcción
        queryPreviewTerminal = new TextArea();
        queryPreviewTerminal.setEditable(false);
        queryPreviewTerminal.setWrapText(true);
        queryPreviewTerminal.setPrefHeight(100);
        queryPreviewTerminal.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; " +
                "-fx-highlight-fill: white; -fx-highlight-text-fill: black; " +
                "-fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px;");

        // Añadir la terminal y los botones a la parte inferior del layout
        VBox bottomContainer = new VBox(10, queryPreviewTerminal, bottomBox);
        layout.setBottom(bottomContainer);
    }

    /**
     * Método para añadir una tabla con información de las columnas.
     * @param tableName El nombre de la tabla.
     * @param columnsData Lista de objetos ColumnDetails que contienen los detalles de cada columna.
     */
    public void addTableColumns(String tableName, ObservableList<ColumnDetails> columnsData) {
        // Crear etiqueta para el nombre de la tabla
        Label tableLabel = new Label("Tabla: " + tableName);
        tableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Crear TableView para mostrar los detalles de las columnas
        TableView<ColumnDetails> tableView = new TableView<>();
        tableView.setItems(columnsData);

        // Configurar el tamaño del TableView para que solo muestre 5 filas visibles
        tableView.setFixedCellSize(35); // Tamaño de celda ampliado
        tableView.setPrefHeight(35 * 5); // Altura para mostrar 5 filas visibles

        // Columna de selección
        TableColumn<ColumnDetails, CheckBox> selectColumn = new TableColumn<>("Seleccionar");
        selectColumn.setCellValueFactory(cellData -> {
            ColumnDetails columnDetails = cellData.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedColumnsWithAliases
                            .computeIfAbsent(tableName, k -> new HashMap<>())
                            .put(columnDetails.getField(), "");
                } else {
                    selectedColumnsWithAliases.getOrDefault(tableName, new HashMap<>()).remove(columnDetails.getField());
                }
                updateQueryPreview(); // Actualizar el preview del query
            });
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });
        selectColumn.setMinWidth(120); // Espacio más amplio para la columna

        // Columna de alias
        TableColumn<ColumnDetails, TextField> aliasColumn = new TableColumn<>("Alias");
        aliasColumn.setCellValueFactory(cellData -> {
            ColumnDetails columnDetails = cellData.getValue();
            TextField aliasField = new TextField();
            aliasField.setPromptText("Alias");
            aliasField.textProperty().addListener((obs, oldText, newText) -> {
                if (selectedColumnsWithAliases.getOrDefault(tableName, new HashMap<>()).containsKey(columnDetails.getField())) {
                    selectedColumnsWithAliases.get(tableName).put(columnDetails.getField(), newText);
                }
                updateQueryPreview(); // Actualizar el preview del query
            });
            return new javafx.beans.property.SimpleObjectProperty<>(aliasField);
        });
        aliasColumn.setMinWidth(120); // Espacio más amplio para la columna

        // Columnas estándar de Field y Type
        TableColumn<ColumnDetails, String> fieldColumn = new TableColumn<>("Field");
        fieldColumn.setCellValueFactory(new PropertyValueFactory<>("field"));
        fieldColumn.setMinWidth(150); // Espacio ampliado

        TableColumn<ColumnDetails, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setMinWidth(150); // Espacio ampliado

        // Añadir solo 4 columnas al TableView
        tableView.getColumns().addAll(selectColumn, aliasColumn, fieldColumn, typeColumn);

        // Añadir TableView y etiqueta al VBox principal
        VBox tableContainer = new VBox(5, tableLabel, tableView);
        tableContainer.setPadding(new Insets(10, 0, 10, 0));
        tablesBox.getChildren().add(tableContainer);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public VBox getTablesBox() {
        return tablesBox;
    }

    public Button getContinueButton() {
        return continueButton;
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    /**
     * Método para obtener las columnas seleccionadas y sus alias.
     */
    public Map<String, Map<String, String>> getSelectedColumnsWithAliases() {
        return selectedColumnsWithAliases;
    }

    /**
     * Actualiza el preview del query en la terminal.
     */
    /**
 * Actualiza el preview del query en la terminal.
 */
private void updateQueryPreview() {
    StringBuilder queryBuilder = new StringBuilder("SELECT ");
    boolean first = true;

    for (Map.Entry<String, Map<String, String>> tableEntry : selectedColumnsWithAliases.entrySet()) {
        String tableName = tableEntry.getKey(); // Nombre de la tabla
        for (Map.Entry<String, String> columnEntry : tableEntry.getValue().entrySet()) {
            if (!first) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(tableName).append(".").append(columnEntry.getKey()); // tabla.columna
            if (!columnEntry.getValue().isEmpty()) {
                queryBuilder.append(" AS ").append(columnEntry.getValue()); // Alias si se asignó
            }
            first = false;
        }
    }

    if (queryBuilder.length() == 7) { // No se ha seleccionado ninguna columna
        queryPreviewTerminal.setText("");
    } else {
        queryPreviewTerminal.setText(queryBuilder.toString());
    }
}

}
