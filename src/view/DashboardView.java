package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
  Vista para el Dashboard de gestión de bases de datos y tablas.
 */
public class DashboardView {
    private BorderPane layout; // Layout principal
    private ComboBox<String> databaseSelector; // ComboBox para seleccionar base de datos
    private ComboBox<String> tableSelector; // ComboBox para seleccionar tabla
    private TableView<ObservableList<String>> tableView; // Tabla para mostrar datos
    private Button addButton, deleteButton, updateButton, goBackButton, createViewButton, viewStructureButton; // Botones CRUD y otros
    private HBox inputFieldsArea; // Área dinámica para los campos de entrada en una sola línea
    private List<TextField> inputFields; // Lista de campos dinámicos de entrada

    /**
     * Constructor que inicializa la vista del Dashboard.
     */
    public DashboardView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Contenedor superior: Selector de base de datos y tabla
        HBox topBox = createTopSection();

        // Contenedor de inputs dinámicos
        VBox centerBox = createCenterSection();

        // Contenedor inferior: Botones CRUD y otros botones
        BorderPane bottomBox = createBottomSection();

        // Configurar layout principal
        layout.setTop(topBox);
        layout.setCenter(centerBox);
        layout.setBottom(bottomBox);

        // Deshabilitar botones por defecto
        disableActionButtons();
    }

    /**
     * Crea la sección superior con ComboBoxes para seleccionar base de datos y tabla.
     *
     * @return HBox con los elementos de selección.
     */
    private HBox createTopSection() {
        HBox topBox = new HBox(15);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER);

        // Selector de base de datos
        Label databaseLabel = new Label("Base de Datos:");
        databaseSelector = new ComboBox<>();
        databaseSelector.setPromptText("Selecciona una base de datos");
        databaseSelector.setPrefWidth(200);

        // Selector de tablas
        Label tableLabel = new Label("Tabla:");
        tableSelector = new ComboBox<>();
        tableSelector.setPromptText("Selecciona una tabla");
        tableSelector.setPrefWidth(200);

        // Botón "Ver Estructura" al lado del selector de tablas
        viewStructureButton = new Button("Ver Estructura");
        viewStructureButton.setStyle("-fx-background-color: lightgray; -fx-text-fill: black;");

        // Añadir componentes al contenedor
        topBox.getChildren().addAll(databaseLabel, databaseSelector, tableLabel, tableSelector, viewStructureButton);

        return topBox;
    }

    /**
     * Crea la sección central con inputs dinámicos y un TableView para mostrar datos de la tabla.
     *
     * @return VBox con los elementos.
     */
    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));
        centerBox.setAlignment(Pos.CENTER);

        // Área dinámica para los inputs en una sola línea
        inputFieldsArea = new HBox(10);
        inputFieldsArea.setPadding(new Insets(10));
        inputFieldsArea.setAlignment(Pos.CENTER_LEFT);

        // Inicializa la lista de campos de entrada
        inputFields = new ArrayList<>();

        // Tabla para mostrar los datos
        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Seleccione una tabla para ver sus datos"));

        // Botón "Crear Vista" debajo de la tabla
        createViewButton = new Button("Crear Vista");
        createViewButton.setMaxWidth(Double.MAX_VALUE); // Ocupa todo el ancho
        createViewButton.setStyle("-fx-background-color: lightblue; -fx-text-fill: black;"); // Estilo opcional

        // Añadir tabla y botón al contenedor
        centerBox.getChildren().addAll(inputFieldsArea, tableView, createViewButton);
        return centerBox;
    }

    /**
     * Crea la sección inferior con botones para operaciones CRUD y otros botones.
     *
     * @return BorderPane con los botones CRUD y "Volver".
     */
    private BorderPane createBottomSection() {
        BorderPane bottomBox = new BorderPane();
        bottomBox.setPadding(new Insets(10));

        // Contenedor de botones CRUD
        HBox crudButtons = new HBox(10);
        crudButtons.setAlignment(Pos.CENTER);

        deleteButton = new Button("Eliminar");
        updateButton = new Button("Modificar");
        addButton = new Button("Añadir");

        // Estilizar botones CRUD
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"); // Botón rojo
        updateButton.setStyle("-fx-background-color: orange; -fx-text-fill: black;"); // Botón amarillo
        addButton.setStyle("-fx-background-color: green; -fx-text-fill: white;"); // Botón verde

        crudButtons.getChildren().addAll(deleteButton, updateButton, addButton);

        // Botón "Volver"
        goBackButton = new Button("Volver");
        goBackButton.setAlignment(Pos.BOTTOM_LEFT);

        // Configuración del BorderPane
        bottomBox.setCenter(crudButtons);
        bottomBox.setLeft(goBackButton);

        return bottomBox;
    }

    /**
     * Actualiza los campos de entrada dinámicos según las columnas de la tabla.
     *
     * @param columnNames Lista con los nombres de las columnas.
     */
    public void updateInputFields(List<String> columnNames) {
        inputFieldsArea.getChildren().clear(); // Limpia el área dinámica
        inputFields.clear(); // Limpia la lista de campos existentes

        for (String columnName : columnNames) {
            TextField textField = new TextField();
            textField.setPromptText(columnName);
            textField.setPrefWidth(150);
            inputFields.add(textField);
            inputFieldsArea.getChildren().add(textField);
        }
    }

    /**
     * Retorna los valores ingresados en los campos de entrada.
     *
     * @return Lista de valores ingresados.
     */
    public List<String> getInputValues() {
        List<String> values = new ArrayList<>();
        for (TextField textField : inputFields) {
            values.add(textField.getText());
        }
        return values;
    }

    /**
     * Deshabilita todos los botones de acción (Eliminar, Modificar, Añadir, Crear Vista).
     */
    public void disableActionButtons() {
        deleteButton.setDisable(true);
        updateButton.setDisable(true);
        addButton.setDisable(true);
        createViewButton.setDisable(true);
        viewStructureButton.setDisable(true);
    }

    /**
     * Habilita todos los botones de acción (Eliminar, Modificar, Añadir, Crear Vista).
     */
    public void enableActionButtons() {
        deleteButton.setDisable(false);
        updateButton.setDisable(false);
        addButton.setDisable(false);
        createViewButton.setDisable(false);
        viewStructureButton.setDisable(false);
    }

    // Getters
    public BorderPane getLayout() {
    if (layout == null) {
        System.out.println("Error: El layout no está inicializado.");
        throw new IllegalStateException("El layout no está inicializado.");
    }
    return layout;
}

    public ComboBox<String> getDatabaseSelector() {
        return databaseSelector;
    }

    public ComboBox<String> getTableSelector() {
        return tableSelector;
    }

    public TableView<ObservableList<String>> getTableView() {
        return tableView;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getGoBackButton() {
        return goBackButton;
    }

    public Button getCreateViewButton() {
        return createViewButton;
    }

    public Button getViewStructureButton() {
        return viewStructureButton;
    }

    public HBox getInputFieldsArea() {
        return inputFieldsArea;}
}