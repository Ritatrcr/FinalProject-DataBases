package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
    private ObservableList<String> availableTables = FXCollections.observableArrayList("Tabla1", "Tabla2", "Tabla3", "Tabla4");

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
        topSection.setPadding(new Insets(10));
        topSection.setAlignment(Pos.CENTER_LEFT);

        databaseSelector = new ComboBox<>();
        databaseSelector.setPromptText("Selecciona una base de datos");
        databaseSelector.setMaxWidth(Double.MAX_VALUE);

        firstTableSelector = new ComboBox<>();
        firstTableSelector.setPromptText("Selecciona la tabla 1");
        firstTableSelector.setMaxWidth(Double.MAX_VALUE);

        secondTableSelector = new ComboBox<>();
        secondTableSelector.setPromptText("Selecciona la tabla 2");
        secondTableSelector.setMaxWidth(Double.MAX_VALUE);

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

        conditionSection = new VBox(10);
        conditionSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        VBox.setVgrow(conditionSection, Priority.ALWAYS);

        Label conditionLabel = new Label("Condiciones:");
        conditionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        conditionLabel.setMaxWidth(Double.MAX_VALUE);

        addConditionButton = new Button("Añadir condición");
        addConditionButton.setMaxWidth(Double.MAX_VALUE);
        addConditionButton.setStyle("-fx-font-size: 12px;");

        centerSection.getChildren().addAll(conditionLabel, conditionSection, addConditionButton);
        return centerSection;
    }

    private VBox createBottomSection() {
    VBox bottomSection = new VBox(15);
    bottomSection.setPadding(new Insets(10));

    Label terminalLabel = new Label("Query MySQL:");
    terminalLabel.setMaxWidth(Double.MAX_VALUE);

    queryTerminal = new TextArea();
    queryTerminal.setEditable(false);
    queryTerminal.setPrefHeight(100);
    queryTerminal.setMaxWidth(Double.MAX_VALUE);

    HBox buttonBox = new HBox(15);
    buttonBox.setAlignment(Pos.CENTER);

    // Botón Ver Resultado
    viewResultButton = new Button("Ver Resultado");
    viewResultButton.setMaxWidth(Double.MAX_VALUE);

    // Botón Volver
    goBackButton = new Button("Volver");
    goBackButton.setMaxWidth(Double.MAX_VALUE);
    
    clearButton = new Button("Limpiar");
    clearButton.setMaxWidth(Double.MAX_VALUE);

    // Añadir los botones al contenedor buttonBox
    buttonBox.getChildren().addAll(viewResultButton, goBackButton,clearButton);

    // Añadir elementos al contenedor bottomSection
    bottomSection.getChildren().addAll(terminalLabel, queryTerminal, buttonBox);
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


    public void addConditionRow() {
        if (conditionSection.getChildren().size() >= 2) {
            addConditionButton.setDisable(true); // Deshabilitar si ya hay 2 condiciones
            return;
        }

        GridPane conditionRow = new GridPane();
        conditionRow.setPadding(new Insets(5));
        conditionRow.setHgap(10); // Espacio horizontal entre columnas
        conditionRow.setVgap(5); // Espacio vertical entre filas
        conditionRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 3; -fx-padding: 5;");
        conditionRow.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33.33);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33.33);
        col2.setHgrow(Priority.ALWAYS);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33.34);
        col3.setHgrow(Priority.ALWAYS);

        conditionRow.getColumnConstraints().addAll(col1, col2, col3);

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

        conditionRow.add(fieldSelector, 0, 0); // Columna 1
        conditionRow.add(operatorSelector, 1, 0); // Columna 2
        conditionRow.add(valueField, 2, 0); // Columna 3

        conditionSection.getChildren().add(conditionRow);

        if (conditionSection.getChildren().size() >= 2) {
            addConditionButton.setDisable(true); // Deshabilitar si hay 2 condiciones
        }
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
        return queryTerminal;
    }

    public VBox getConditionSection() {
        return conditionSection;
    }
    public Button getClearButton() {
    return clearButton;
}

    
}
