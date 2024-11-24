package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UserQueryView {
    private BorderPane layout;
    private ComboBox<String> databaseSelector;
    private ComboBox<String> firstTableSelector, secondTableSelector;
    private Button viewTableButton, viewResultButton, addConditionButton, executeQueryButton;
    private TextArea queryTerminal;
    private VBox conditionSection;
    private TextArea tableStructureArea;

    public UserQueryView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        HBox topSection = createTopSection();
        VBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        layout.setTop(topSection);
        layout.setCenter(centerSection);
        layout.setBottom(bottomSection);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));
        topSection.setAlignment(Pos.CENTER_LEFT);

        databaseSelector = new ComboBox<>();
        databaseSelector.setPromptText("Selecciona una base de datos");
        databaseSelector.setPrefWidth(200);

        firstTableSelector = new ComboBox<>();
        firstTableSelector.setPromptText("Selecciona la primera tabla");
        firstTableSelector.setPrefWidth(200);

        secondTableSelector = new ComboBox<>();
        secondTableSelector.setPromptText("Selecciona la segunda tabla (opcional)");
        secondTableSelector.setPrefWidth(200);

        viewTableButton = new Button("Ver Tabla");

        topSection.getChildren().addAll(databaseSelector, firstTableSelector, secondTableSelector, viewTableButton);
        return topSection;
    }

    private VBox createCenterSection() {
        VBox centerSection = new VBox(10);
        centerSection.setPadding(new Insets(10));

        // Estructura de la tabla
        Label structureLabel = new Label("Estructura de la tabla:");
        tableStructureArea = new TextArea();
        tableStructureArea.setEditable(false);
        tableStructureArea.setPrefHeight(150);

        conditionSection = new VBox(10);
        conditionSection.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        Label conditionLabel = new Label("Condiciones:");
        addConditionButton = new Button("Añadir condición");

        centerSection.getChildren().addAll(structureLabel, tableStructureArea, conditionLabel, conditionSection, addConditionButton);
        return centerSection;
    }

    private VBox createBottomSection() {
        VBox bottomSection = new VBox(15);
        bottomSection.setPadding(new Insets(10));

        Label terminalLabel = new Label("Query MySQL:");
        queryTerminal = new TextArea();
        queryTerminal.setEditable(false);
        queryTerminal.setPrefHeight(100);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        executeQueryButton = new Button("Ejecutar Query");
        viewResultButton = new Button("Ver Resultado");

        buttonBox.getChildren().addAll(executeQueryButton, viewResultButton);
        bottomSection.getChildren().addAll(terminalLabel, queryTerminal, buttonBox);
        return bottomSection;
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

    public Button getAddConditionButton() {
        return addConditionButton;
    }

    public Button getExecuteQueryButton() {
        return executeQueryButton;
    }

    public TextArea getQueryTerminal() {
        return queryTerminal;
    }

    public VBox getConditionSection() {
        return conditionSection;
    }

    public void showTableStructure(String structureText) {
        tableStructureArea.setText(structureText);
    }
}
