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
 * Vista para el Dashboard de gestión de bases de datos y tablas.
 */
public class DashboardView {
    private ScrollPane scrollPane; // ScrollPane para habilitar desplazamiento
    private BorderPane layout; // Layout principal
    private ComboBox<String> databaseSelector; // ComboBox para seleccionar base de datos
    private ComboBox<String> tableSelector; // ComboBox para seleccionar tabla
    private TableView<ObservableList<String>> tableView; // Tabla para mostrar datos
    private Button addButton, deleteButton, updateButton, loadTableButton, goBackButton; // Botones CRUD y "Volver"
    private VBox inputFieldsArea; // Área dinámica para los campos de entrada
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

        // Contenedor inferior: Botones CRUD y botón "Volver"
        BorderPane bottomBox = createBottomSection();

        // Configurar layout principal
        layout.setTop(topBox);
        layout.setCenter(centerBox);
        layout.setBottom(bottomBox);

        // Envolver el layout en un ScrollPane
        scrollPane = new ScrollPane();
        scrollPane.setContent(layout);
        scrollPane.setFitToWidth(true); // Ajustar al ancho de la ventana
        scrollPane.setFitToHeight(true); // Ajustar al alto de la ventana
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

        // Botón para cargar datos de la tabla seleccionada
        loadTableButton = new Button("Cargar Datos");

        // Añadir componentes al contenedor
        topBox.getChildren().addAll(databaseLabel, databaseSelector, tableLabel, tableSelector, loadTableButton);

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

        // Área dinámica para los inputs
        inputFieldsArea = new VBox(10); // Cambiado a VBox para mejor distribución
        inputFieldsArea.setPadding(new Insets(10));
        inputFieldsArea.setAlignment(Pos.TOP_LEFT);

        // Inicializa la lista de campos de entrada
        inputFields = new ArrayList<>();

        // Tabla para mostrar los datos
        tableView = new TableView<>();
        tableView.setPlaceholder(new Label("Seleccione una tabla para ver sus datos"));

        centerBox.getChildren().addAll(inputFieldsArea, tableView);
        return centerBox;
    }

    /**
     * Crea la sección inferior con botones para operaciones CRUD y el botón "Volver".
     *
     * @return BorderPane con los botones CRUD y "Volver".
     */
    private BorderPane createBottomSection() {
        BorderPane bottomBox = new BorderPane();
        bottomBox.setPadding(new Insets(10));

        // Botones CRUD
        HBox crudButtons = new HBox(10);
        crudButtons.setAlignment(Pos.CENTER);

        addButton = new Button("Añadir");
        deleteButton = new Button("Eliminar");
        updateButton = new Button("Actualizar");
        crudButtons.getChildren().addAll(addButton, deleteButton, updateButton);

        // Botón "Volver"
        goBackButton = new Button("Volver");
        goBackButton.setAlignment(Pos.BOTTOM_LEFT);

        // Colocar los botones en el BorderPane
        bottomBox.setCenter(crudButtons);
        bottomBox.setLeft(goBackButton); // Botón "Volver" en la esquina inferior izquierda

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

        // Genera un campo de texto para cada columna en formato fila sin etiquetas
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        for (String columnName : columnNames) {
            TextField textField = new TextField();
            textField.setPromptText(columnName); // Utiliza el nombre de la columna como placeholder
            textField.setPrefWidth(150);
            row.getChildren().add(textField);

            // Añadir el campo a la lista
            inputFields.add(textField);
        }

        inputFieldsArea.getChildren().add(row);
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
     * Retorna el ScrollPane principal para la vista.
     *
     * @return El ScrollPane con el contenido.
     */
    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    // Getters para otros componentes
    public ComboBox<String> getDatabaseSelector() {
        return databaseSelector;
    }

    public ComboBox<String> getTableSelector() {
        return tableSelector;
    }

    public Button getLoadTableButton() {
        return loadTableButton;
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
}