package controllers;

import modelo.DatabaseManager;
import view.DashboardView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {
    private DashboardView view;
    private DatabaseManager dbManager;

    /**
     * Constructor del controlador.
     *
     * @param view      La vista del Dashboard.
     * @param dbManager Instancia de DatabaseManager.
     */
    public DashboardController(DashboardView view, DatabaseManager dbManager) {
        this.view = view;
        this.dbManager = dbManager;
    }

    /**
     * Configura los eventos y la lógica de la vista del Dashboard.
     */
    public void setup() {
        // Configura eventos
        view.getLoadDatabaseButton().setOnAction(this::loadTables);
        view.getLoadTableButton().setOnAction(this::loadTableData);
        view.getAddButton().setOnAction(this::addRecord);
        view.getDeleteButton().setOnAction(this::deleteRecord);
        view.getUpdateButton().setOnAction(this::updateRecord);

        // Carga las bases de datos al inicio
        loadDatabases();
    }

    /**
     * Carga las bases de datos disponibles en el ComboBox.
     */
    private void loadDatabases() {
        List<String> databases = dbManager.getDatabaseNames();
        if (databases.isEmpty()) {
            showAlert("Error", "No se encontraron bases de datos en el servidor.");
        } else {
            view.getDatabaseSelector().getItems().setAll(databases);
        }
    }

    /**
     * Carga las tablas de la base de datos seleccionada.
     *
     * @param event Evento de clic del botón "Cargar Tablas".
     */
    private void loadTables(ActionEvent event) {
        String selectedDatabase = view.getDatabaseSelector().getValue();
        if (selectedDatabase == null || selectedDatabase.isEmpty()) {
            showAlert("Error", "Por favor, selecciona una base de datos.");
            return;
        }

        List<String> tables = dbManager.getTableNames(selectedDatabase);
        if (tables.isEmpty()) {
            showAlert("Información", "No se encontraron tablas en la base de datos seleccionada.");
        } else {
            view.getTableSelector().getItems().setAll(tables);
        }
    }

    /**
     * Carga los datos de la tabla seleccionada en el TableView.
     *
     * @param event Evento de clic del botón "Cargar Datos".
     */
    private void loadTableData(ActionEvent event) {
        String selectedDatabase = view.getDatabaseSelector().getValue();
        String selectedTable = view.getTableSelector().getValue();

        if (selectedDatabase == null || selectedDatabase.isEmpty()) {
            showAlert("Error", "Por favor, selecciona una base de datos.");
            return;
        }

        if (selectedTable == null || selectedTable.isEmpty()) {
            showAlert("Error", "Por favor, selecciona una tabla.");
            return;
        }

        // Limpia la tabla y los campos de entrada antes de cargar nuevos datos
        view.getTableView().getColumns().clear();
        view.getTableView().getItems().clear();

        try {
            ResultSet rs = dbManager.getTableData(selectedDatabase, selectedTable);
            int columnCount = rs.getMetaData().getColumnCount();

            // Obtener los nombres de las columnas y actualizarlos en los campos dinámicos
            List<String> columnNames = dbManager.getColumnNames(selectedDatabase, selectedTable);
            view.updateInputFields(columnNames);

            // Crear columnas dinámicamente
            for (int i = 1; i <= columnCount; i++) {
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(rs.getMetaData().getColumnName(i));
                final int colIndex = i - 1;
                column.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty(param.getValue().get(colIndex)));
                view.getTableView().getColumns().add(column);
            }

            // Cargar filas en el TableView
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                view.getTableView().getItems().add(row);
            }
        } catch (SQLException e) {
            showAlert("Error", "Error cargando datos de la tabla: " + e.getMessage());
        }
    }

    /**
     * Añade un nuevo registro a la tabla seleccionada.
     */
    private void addRecord(ActionEvent event) {
        List<String> inputValues = view.getInputValues();

        // Verifica si algún campo está vacío
        if (inputValues.stream().anyMatch(String::isEmpty)) {
            showAlert("Error", "Todos los campos son obligatorios.");
            return;
        }

        String selectedTable = view.getTableSelector().getValue();
        if (selectedTable == null || selectedTable.isEmpty()) {
            showAlert("Error", "Por favor, selecciona una tabla.");
            return;
        }

        // Construir e insertar el registro
        String query = "INSERT INTO " + selectedTable + " VALUES (" +
                inputValues.stream().map(value -> "'" + value + "'").reduce((a, b) -> a + ", " + b).orElse("") + ")";
        if (dbManager.executeUpdate(query)) {
            showAlert("Éxito", "Registro añadido correctamente.");
            view.getLoadTableButton().fire(); // Recargar los datos de la tabla
        } else {
            showAlert("Error", "No se pudo añadir el registro.");
        }
    }

    /**
     * Elimina un registro seleccionado de la tabla.
     */
    private void deleteRecord(ActionEvent event) {
        String selectedTable = view.getTableSelector().getValue();
        ObservableList<String> selectedRow = view.getTableView().getSelectionModel().getSelectedItem();
        if (selectedTable == null || selectedRow == null) {
            showAlert("Error", "Por favor, selecciona una tabla y un registro para eliminar.");
            return;
        }

        // Suponemos que la primera columna es la clave primaria
        String primaryKeyValue = selectedRow.get(0);
        String query = "DELETE FROM " + selectedTable + " WHERE id = '" + primaryKeyValue + "'";
        if (dbManager.executeUpdate(query)) {
            showAlert("Éxito", "Registro eliminado correctamente.");
            view.getLoadTableButton().fire(); // Recargar los datos de la tabla
        } else {
            showAlert("Error", "No se pudo eliminar el registro.");
        }
    }

    /**
     * Actualiza un registro seleccionado en la tabla.
     */
    private void updateRecord(ActionEvent event) {
        String selectedTable = view.getTableSelector().getValue();
        ObservableList<String> selectedRow = view.getTableView().getSelectionModel().getSelectedItem();
        if (selectedTable == null || selectedRow == null) {
            showAlert("Error", "Por favor, selecciona una tabla y un registro para actualizar.");
            return;
        }

        // Mostrar un cuadro de diálogo para ingresar los nuevos valores
        List<String> inputValues = view.getInputValues();
        if (inputValues.stream().anyMatch(String::isEmpty)) {
            showAlert("Error", "Todos los campos son obligatorios.");
            return;
        }

        // Construir consulta de actualización
        String query = "UPDATE " + selectedTable + " SET " +
                inputValues.stream()
                        .map(value -> "column_name='" + value + "'") // Ajusta para asignar los valores a columnas específicas
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("") +
                " WHERE id = '" + selectedRow.get(0) + "'"; // Clave primaria asumida en la primera columna
        if (dbManager.executeUpdate(query)) {
            showAlert("Éxito", "Registro actualizado correctamente.");
            view.getLoadTableButton().fire(); // Recargar los datos de la tabla
        } else {
            showAlert("Error", "No se pudo actualizar el registro.");
        }
    }

    /**
     * Muestra un cuadro de diálogo de alerta con un mensaje.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
