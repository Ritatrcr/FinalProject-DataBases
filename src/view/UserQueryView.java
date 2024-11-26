package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UserQueryView {
    private BorderPane layout;
    private ComboBox<String> databaseSelector;
    private ComboBox<String> firstTableSelector, secondTableSelector;
    private Button viewTableButton, viewResultButton, goBackButton, addConditionButton,clearButton;
    private TextArea queryTerminal;
    private VBox conditionSection;
    

    // Lista de tablas originales para restaurar opciones
   

    public UserQueryView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox topSection = createTopSection();
        VBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        layout.setTop(topSection);
        layout.setCenter(centerSection);
        layout.setBottom(bottomSection);

        // Inicialmente deshabilitar los botones y selectores hasta que se seleccione una base de datos
        firstTableSelector.setDisable(true);
        secondTableSelector.setDisable(true);
        viewTableButton.setDisable(true);

        // Listener para habilitar botones y selectores cuando se seleccione una base de datos
        databaseSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean isDatabaseSelected = newValue != null && !newValue.isEmpty();
            firstTableSelector.setDisable(!isDatabaseSelected);
            secondTableSelector.setDisable(!isDatabaseSelected);
            viewTableButton.setDisable(!isDatabaseSelected);
        });

        // Listener para actualizar el segundo selector de tabla
        firstTableSelector.valueProperty().addListener((observable, oldValue, newValue) -> updateSecondTableSelector());
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(80));
        topSection.setAlignment(Pos.CENTER_LEFT);

        databaseSelector = new ComboBox<>();
        databaseSelector.setPromptText("Selecciona una base de datos");
        databaseSelector.setMaxWidth(Double.MAX_VALUE);

        firstTableSelector = new ComboBox<>();
        firstTableSelector.setPromptText("Selecciona la tabla 1");
        firstTableSelector.setMaxWidth(200); // Establece un ancho máximo más pequeño
        firstTableSelector.setPrefWidth(150);

        secondTableSelector = new ComboBox<>();
        secondTableSelector.setPromptText("Selecciona la tabla 2");
        secondTableSelector.setMaxWidth(200); // Establece un ancho máximo más pequeño
        secondTableSelector.setPrefWidth(150); 

        viewTableButton = new Button("Ver Tabla(s)");
        viewTableButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(databaseSelector, Priority.ALWAYS);
        HBox.setHgrow(firstTableSelector, Priority.ALWAYS);
        HBox.setHgrow(secondTableSelector, Priority.ALWAYS);
        HBox.setHgrow(viewTableButton, Priority.ALWAYS);

        topSection.getChildren().addAll(databaseSelector, firstTableSelector, secondTableSelector, viewTableButton);
        return topSection;
    }

    private VBox createCenterSection() {
    VBox centerSection = new VBox(10);
    centerSection.setPadding(new Insets(10));
    centerSection.setFillWidth(true);

    // Sección de condiciones con borde y estilo
    conditionSection = new VBox(5);
    conditionSection.setPrefHeight(100); // Altura preferida más pequeña
    conditionSection.setMaxHeight(100); // Altura máxima ajustada
    conditionSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
    VBox.setVgrow(conditionSection, Priority.NEVER); // Evitar que crezca más allá de la altura establecida

    // Etiqueta de condiciones
    Label conditionLabel = new Label("Condiciones:");
    conditionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    conditionLabel.setMaxWidth(Double.MAX_VALUE);

    // Contenedor superior para la etiqueta y el botón "+"
    HBox headerBox = new HBox();
    headerBox.setAlignment(Pos.CENTER_LEFT); // Etiqueta alineada a la izquierda
    headerBox.setSpacing(10); // Espaciado entre elementos

    // Botón para añadir condición (+)
    addConditionButton = new Button("+");
    addConditionButton.setStyle("-fx-background-color: lightblue; -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5;");
    addConditionButton.setPrefSize(30, 30); // Tamaño del botón
    addConditionButton.setDisable(true); // Deshabilitado por defecto
    HBox.setMargin(addConditionButton, new Insets(0, 0, 0, 10)); // Márgenes para ajustar posición

    // Listener para habilitar/deshabilitar el botón cuando ambas tablas están seleccionadas
    firstTableSelector.valueProperty().addListener((observable, oldValue, newValue) -> toggleAddConditionButton());
    secondTableSelector.valueProperty().addListener((observable, oldValue, newValue) -> toggleAddConditionButton());

    // Añadir etiqueta y botón al header
    headerBox.getChildren().addAll(conditionLabel, addConditionButton);
    headerBox.setAlignment(Pos.CENTER_LEFT); // Botón alineado a la derecha en el contenedor

    // Añadir la cabecera y la sección de condiciones al contenedor principal
    centerSection.getChildren().addAll(headerBox, conditionSection);

    return centerSection;
}


    /**
     * Habilita o deshabilita el botón de añadir condición según si hay dos tablas seleccionadas.
     */
    private void toggleAddConditionButton() {
        boolean bothTablesSelected = firstTableSelector.getValue() != null && secondTableSelector.getValue() != null;
        addConditionButton.setDisable(!bothTablesSelected);
    }


    private VBox createBottomSection() {
    VBox bottomSection = new VBox(15);
    bottomSection.setPadding(new Insets(10));


    // Compartimiento para mostrar el query en tiempo real
    VBox queryDisplayContainer = new VBox();
    queryDisplayContainer.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 5; -fx-padding: 10;");
    queryDisplayContainer.setPadding(new Insets(5));
    Label queryDisplayLabel = new Label("Query en tiempo real MySQL:");
    TextArea queryRealTimeDisplay = new TextArea();
    queryRealTimeDisplay.setEditable(false);
    queryRealTimeDisplay.setPrefHeight(100);
    queryDisplayContainer.getChildren().addAll(queryDisplayLabel, queryRealTimeDisplay);

    // TextArea para la terminal principal
    queryTerminal = new TextArea();
    queryTerminal.setEditable(false);
    queryTerminal.setPrefHeight(200); // Aumentar el tamaño del campo de texto
    queryTerminal.setWrapText(true); // Ajustar texto al ancho
    queryTerminal.setStyle("-fx-border-color: lightblue; -fx-border-width: 1px; -fx-border-radius: 5;");

    // Botón Volver en la parte inferior izquierda
    HBox goBackBox = new HBox();
    goBackBox.setAlignment(Pos.BOTTOM_LEFT);
    goBackButton = new Button("Volver");
    goBackButton.setStyle("-fx-background-color: lightgray; -fx-border-color: gray; -fx-font-size: 14px;");
    goBackButton.setMaxWidth(Double.MAX_VALUE);
    goBackBox.getChildren().add(goBackButton);

    // Botones Limpiar y Ver Resultado centrados
    HBox centerButtonsBox = new HBox(15);
    centerButtonsBox.setAlignment(Pos.CENTER);

    clearButton = new Button("Limpiar");
    clearButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14px;");
    clearButton.setMaxWidth(150);

    viewResultButton = new Button("Ver Resultado");
    viewResultButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 14px;");
    viewResultButton.setMaxWidth(150);

    centerButtonsBox.getChildren().addAll(clearButton, viewResultButton);

    // Añadir elementos al contenedor bottomSection
    bottomSection.getChildren().addAll( queryDisplayContainer, queryTerminal, goBackBox, centerButtonsBox);

    return bottomSection;
}






    private void updateSecondTableSelector() {
    String selectedTable1 = firstTableSelector.getValue();
    if (selectedTable1 != null) {
        // Obtener todas las tablas de la base de datos seleccionada
        ObservableList<String> currentTables = firstTableSelector.getItems();

        // Filtrar las tablas para excluir la seleccionada en Tabla 1
        ObservableList<String> filteredTables = FXCollections.observableArrayList(currentTables);
        filteredTables.remove(selectedTable1);

        // Actualizar las opciones del segundo selector
        secondTableSelector.setItems(filteredTables);

        // Limpiar la selección actual si no está en las opciones
        if (!filteredTables.contains(secondTableSelector.getValue())) {
            secondTableSelector.getSelectionModel().clearSelection();
        }

        // Habilitar el segundo selector si hay tablas disponibles
        secondTableSelector.setDisable(filteredTables.isEmpty());
    } else {
        // Si no hay selección en Tabla 1, restaurar todas las tablas en Tabla 2
        secondTableSelector.setItems(FXCollections.observableArrayList(firstTableSelector.getItems()));
        secondTableSelector.setDisable(true); // Deshabilitar hasta que se seleccione Tabla 1
    }
}


    public void addConditionRow(TextArea queryRealTimeDisplay) {
    GridPane conditionRow = new GridPane();
    conditionRow.setPadding(new Insets(5));
    conditionRow.setHgap(10);
    conditionRow.setVgap(5);
    conditionRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 3; -fx-padding: 5;");
    conditionRow.setMaxWidth(Double.MAX_VALUE);

    ComboBox<String> fieldSelector = new ComboBox<>();
    fieldSelector.setPromptText("Campo");
    fieldSelector.setMaxWidth(Double.MAX_VALUE);

    ComboBox<String> operatorSelector = new ComboBox<>();
    operatorSelector.setPromptText("Operador");
    operatorSelector.getItems().addAll("=", "LIKE", "<", ">", "<=", ">=", "<>", "IS NULL", "IS NOT NULL");
    operatorSelector.setMaxWidth(Double.MAX_VALUE);

    ComboBox<String> valueField = new ComboBox<>();
    valueField.setPromptText("Valor");
    valueField.setMaxWidth(Double.MAX_VALUE);

    // Añadir los ComboBox al GridPane
    conditionRow.add(fieldSelector, 0, 0);
    conditionRow.add(operatorSelector, 1, 0);
    conditionRow.add(valueField, 2, 0);

    // Agregar listeners para actualizar el query en tiempo real
    fieldSelector.valueProperty().addListener((observable, oldValue, newValue) -> updateQueryRealTime(queryRealTimeDisplay));
    operatorSelector.valueProperty().addListener((observable, oldValue, newValue) -> updateQueryRealTime(queryRealTimeDisplay));
    valueField.valueProperty().addListener((observable, oldValue, newValue) -> updateQueryRealTime(queryRealTimeDisplay));

    // Añadir la fila al contenedor de condiciones
    conditionSection.getChildren().add(conditionRow);
}

    
    
    
    private void updateQueryRealTime(TextArea queryRealTimeDisplay) {
    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ");

    // Agregar tablas seleccionadas al query
    if (firstTableSelector.getValue() != null) {
        queryBuilder.append(firstTableSelector.getValue());
    }

    if (secondTableSelector.getValue() != null) {
        queryBuilder.append(", ").append(secondTableSelector.getValue());
    }

    // Construir condiciones WHERE
    if (!conditionSection.getChildren().isEmpty()) {
        queryBuilder.append(" WHERE ");
        for (Node node : conditionSection.getChildren()) {
            if (node instanceof GridPane) {
                GridPane conditionRow = (GridPane) node;

                // Recuperar valores de los ComboBox en la fila
                ComboBox<String> fieldSelector = (ComboBox<String>) conditionRow.getChildren().get(0);
                ComboBox<String> operatorSelector = (ComboBox<String>) conditionRow.getChildren().get(1);
                ComboBox<String> valueField = (ComboBox<String>) conditionRow.getChildren().get(2);

                if (fieldSelector.getValue() != null && operatorSelector.getValue() != null && valueField.getValue() != null) {
                    queryBuilder.append(fieldSelector.getValue())
                                .append(" ")
                                .append(operatorSelector.getValue())
                                .append(" ")
                                .append(valueField.getValue())
                                .append(" AND ");
                }
            }
        }

        // Eliminar el último " AND " si existe
        if (queryBuilder.toString().endsWith(" AND ")) {
            queryBuilder.setLength(queryBuilder.length() - 5);
        }
    }

    // Mostrar el query en tiempo real
    queryRealTimeDisplay.setText(queryBuilder.toString());
}


    public void clearSelections() {
    // Limpiar selección de tablas
    firstTableSelector.getSelectionModel().clearSelection();
    secondTableSelector.getSelectionModel().clearSelection();

    // Limpiar condiciones añadidas
    conditionSection.getChildren().clear();

    // Habilitar botón de añadir condición
    addConditionButton.setDisable(false);

    // Limpiar terminal del query
    queryTerminal.clear();
}

    public BorderPane getLayout() {
        return layout;
    }

    public ComboBox<String> getDatabaseSelector() {
        return databaseSelector;
    }

    public ComboBox<String> getFirstTableSelector() {
        return firstTableSelector;
    }

    public ComboBox<String> getSecondTableSelector() {
        return secondTableSelector;
    }

    public Button getViewTableButton() {
        return viewTableButton;
    }

    public Button getViewResultButton() {
        return viewResultButton;
    }

    public Button getGoBackButton() {
        return goBackButton;
    }

    public Button getAddConditionButton() {
        return addConditionButton;
    }
    public TextArea getQueryTerminal() {
    if (queryTerminal == null) {
        queryTerminal = new TextArea(); // Inicializar si es nula
        queryTerminal.setEditable(false);
        queryTerminal.setPrefHeight(100);
    }
    return queryTerminal;
}


    public VBox getConditionSection() {
        return conditionSection;
    }
    public Button getClearButton() {
    return clearButton;
}

    
}
