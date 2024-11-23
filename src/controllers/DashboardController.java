package controllers;

import finalproject.FinalProject;
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
        view.getDatabaseSelector().setOnAction(event -> loadTables());
        view.getLoadTableButton().setOnAction(this::loadTableData);
        view.getAddButton().setOnAction(this::addRecord);
        view.getDeleteButton().setOnAction(this::deleteRecord);
        view.getUpdateButton().setOnAction(this::updateRecord);
        view.getGoBackButton().setOnAction(event -> {
        // Cambiar a la vista de selección de usuario
        FinalProject.showUserSelectionScene(dbManager);
    });
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
     */
    private void loadTables() {
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
     * Muestra un cuadro de diálogo de alerta con un mensaje.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addRecord(ActionEvent event) {
    String selectedDatabase = view.getDatabaseSelector().getValue();
    String selectedTable = view.getTableSelector().getValue();

    // Validar selección de base de datos y tabla
    if (selectedDatabase == null || selectedDatabase.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una base de datos.");
        return;
    }

    if (selectedTable == null || selectedTable.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una tabla.");
        return;
    }

    // Obtener los valores de los campos de entrada
    List<String> inputValues = view.getInputValues();

    // Validar que no haya campos vacíos
    if (inputValues.contains("") || inputValues.contains(null)) {
        showAlert("Error", "Por favor, completa todos los campos antes de añadir.");
        return;
    }

    // Obtener los nombres de las columnas
    List<String> columnNames = dbManager.getColumnNames(selectedDatabase, selectedTable);

    if (columnNames.size() != inputValues.size()) {
        showAlert("Error", "El número de columnas no coincide con el número de valores proporcionados.");
        return;
    }

    // Llamar al método del DatabaseManager para insertar el registro
    if (dbManager.insertRecord(selectedDatabase, selectedTable, columnNames, inputValues)) {
        showAlert("Éxito", "Registro añadido correctamente.");
        loadTableData(null); // Recargar datos de la tabla para reflejar el cambio
    } else {
        showAlert("Error", "No se pudo añadir el registro. Revisa los datos e inténtalo de nuevo.");
    }
}
    
    



    private void deleteRecord(ActionEvent event) {
    String selectedDatabase = view.getDatabaseSelector().getValue();
    String selectedTable = view.getTableSelector().getValue();

    // Validar selección de base de datos y tabla
    if (selectedDatabase == null || selectedDatabase.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una base de datos.");
        return;
    }

    if (selectedTable == null || selectedTable.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una tabla.");
        return;
    }

    // Obtener la fila seleccionada en el TableView
    ObservableList<String> selectedRow = view.getTableView().getSelectionModel().getSelectedItem();

    if (selectedRow == null) {
        showAlert("Error", "Por favor, selecciona un registro para eliminar.");
        return;
    }

    // Obtener los nombres de las columnas y los valores de la fila seleccionada
    List<String> columnNames = dbManager.getColumnNames(selectedDatabase, selectedTable);

    // Construir una cláusula WHERE para identificar el registro
    StringBuilder whereClause = new StringBuilder();
    for (int i = 0; i < columnNames.size(); i++) {
        String columnName = columnNames.get(i);
        String value = selectedRow.get(i);

        // Manejar valores NULL
        if (value == null || value.trim().isEmpty()) {
            whereClause.append(columnName).append(" IS NULL AND ");
        } else {
            whereClause.append(columnName).append(" = '").append(value.replace("'", "''")).append("' AND ");
        }
    }

    // Eliminar el último " AND "
    String finalWhereClause = whereClause.substring(0, whereClause.length() - 5);

    // Construir y ejecutar el comando SQL DELETE
    String sql = "DELETE FROM " + selectedDatabase + "." + selectedTable + " WHERE " + finalWhereClause;

    if (dbManager.executeUpdate(sql)) {
        showAlert("Éxito", "Registro eliminado correctamente.");
        loadTableData(null); // Recargar los datos de la tabla para reflejar el cambio
    } else {
        showAlert("Error", "No se pudo eliminar el registro. Revisa los datos e inténtalo de nuevo.");
    }
}


    private void updateRecord(ActionEvent event) {
    String selectedDatabase = view.getDatabaseSelector().getValue();
    String selectedTable = view.getTableSelector().getValue();

    // Validar selección de base de datos y tabla
    if (selectedDatabase == null || selectedDatabase.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una base de datos.");
        return;
    }

    if (selectedTable == null || selectedTable.isEmpty()) {
        showAlert("Error", "Por favor, selecciona una tabla.");
        return;
    }

    // Obtener la fila seleccionada en el TableView
    ObservableList<String> selectedRow = view.getTableView().getSelectionModel().getSelectedItem();

    if (selectedRow == null) {
        showAlert("Error", "Por favor, selecciona un registro para actualizar.");
        return;
    }

    // Obtener los nombres de las columnas y los valores nuevos de los inputs
    List<String> columnNames = dbManager.getColumnNames(selectedDatabase, selectedTable);
    List<String> inputValues = view.getInputValues();

    if (inputValues.size() != columnNames.size()) {
        showAlert("Error", "El número de columnas no coincide con los valores proporcionados.");
        return;
    }

    // Construir la cláusula SET con los nuevos valores
    StringBuilder setClause = new StringBuilder();
    for (int i = 0; i < columnNames.size(); i++) {
        String columnName = columnNames.get(i);
        String value = inputValues.get(i);

        if (value == null || value.trim().isEmpty()) {
            setClause.append(columnName).append(" = NULL, ");
        } else {
            setClause.append(columnName).append(" = '").append(value.replace("'", "''")).append("', ");
        }
    }

    // Eliminar la última coma y espacio extra
    String finalSetClause = setClause.substring(0, setClause.length() - 2);

    // Construir la cláusula WHERE para identificar el registro
    StringBuilder whereClause = new StringBuilder();
    for (int i = 0; i < columnNames.size(); i++) {
        String columnName = columnNames.get(i);
        String value = selectedRow.get(i);

        if (value == null || value.trim().isEmpty()) {
            whereClause.append(columnName).append(" IS NULL AND ");
        } else {
            whereClause.append(columnName).append(" = '").append(value.replace("'", "''")).append("' AND ");
        }
    }

    // Eliminar el último " AND "
    String finalWhereClause = whereClause.substring(0, whereClause.length() - 5);

    // Construir y ejecutar el comando SQL UPDATE
    String sql = "UPDATE " + selectedDatabase + "." + selectedTable + " SET " + finalSetClause + " WHERE " + finalWhereClause;

    if (dbManager.executeUpdate(sql)) {
        showAlert("Éxito", "Registro actualizado correctamente.");
        loadTableData(null); // Recargar los datos de la tabla para reflejar los cambios
    } else {
        showAlert("Error", "No se pudo actualizar el registro. Revisa los datos e inténtalo de nuevo.");
    }
}

}
