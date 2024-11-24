package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import modelo.DatabaseManager;
import view.UserQueryView;
import view.TableFieldsView;
import view.ResultView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserQueryController {
    private UserQueryView view;
    private DatabaseManager dbManager;
    private TableFieldsView tableView;
    private ResultView resultView;
    private Scene mainScene;
    private StringBuilder queryBuilder;

    public UserQueryController(UserQueryView view, DatabaseManager dbManager, Scene mainScene) {
        this.view = view;
        this.dbManager = dbManager;
        this.mainScene = mainScene;
        this.tableView = new TableFieldsView();
        this.resultView = new ResultView();
        this.queryBuilder = new StringBuilder();
        setup();
    }

    private void setup() {
        loadDatabases();
        view.getDatabaseSelector().setOnAction(event -> loadTables());
        view.getFirstTableSelector().setOnAction(event -> {
            try {
                displaySelectedTablesStructure();
            } catch (SQLException ex) {
                Logger.getLogger(UserQueryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            resetConditions();
            updateQuery();
        });
        view.getSecondTableSelector().setOnAction(event -> {
            try {
                displaySelectedTablesStructure();
            } catch (SQLException ex) {
                Logger.getLogger(UserQueryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            resetConditions();
            updateQuery();
        });
        view.getViewTableButton().setOnAction(event -> navigateToTableFieldsView());
        view.getViewResultButton().setOnAction(event -> {
            try {
                navigateToResultView();
            } catch (SQLException e) {
                handleError("Error navegando a la vista de resultados", e);
            }
        });
        view.getExecuteQueryButton().setOnAction(event -> {
            try {
                executeQuery();
            } catch (SQLException e) {
                handleError("Error ejecutando el query", e);
            }
        });
        view.getAddConditionButton().setOnAction(event -> addConditionRow());
    }

    private void loadDatabases() {
        List<String> databases = dbManager.getDatabaseNames();
        if (!databases.isEmpty()) {
            ObservableList<String> observableDatabases = FXCollections.observableArrayList(databases);
            view.getDatabaseSelector().setItems(observableDatabases);
        }
    }

    private void loadTables() {
        String database = view.getDatabaseSelector().getValue();
        if (database == null) return;

        List<String> tables = dbManager.getTableNames(database);
        ObservableList<String> observableTables = FXCollections.observableArrayList(tables);
        view.getFirstTableSelector().setItems(observableTables);
        view.getSecondTableSelector().setItems(observableTables);
    }

    private void displaySelectedTablesStructure() throws SQLException {
        String database = view.getDatabaseSelector().getValue();
        if (database == null || database.isEmpty()) {
            return;
        }
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        StringBuilder sb = new StringBuilder();

        if (table1 != null && !table1.isEmpty()) {
            List<String> columnNames1 = dbManager.getColumnNames(database, table1);
            List<String> columnTypes1 = dbManager.getColumnTypes(database, table1);
            sb.append("Estructura de la tabla ").append(table1).append(":\n");
            for (int i = 0; i < columnNames1.size(); i++) {
                sb.append(columnNames1.get(i)).append(" - ").append(columnTypes1.get(i)).append("\n");
            }
            sb.append("\n");
        }
        if (table2 != null && !table2.isEmpty()) {
            List<String> columnNames2 = dbManager.getColumnNames(database, table2);
            List<String> columnTypes2 = dbManager.getColumnTypes(database, table2);
            sb.append("Estructura de la tabla ").append(table2).append(":\n");
            for (int i = 0; i < columnNames2.size(); i++) {
                sb.append(columnNames2.get(i)).append(" - ").append(columnTypes2.get(i)).append("\n");
            }
            sb.append("\n");
        }
        view.showTableStructure(sb.toString());
    }

    private void navigateToTableFieldsView() {
        String database = view.getDatabaseSelector().getValue();
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        if (database == null || table1 == null) {
            handleError("Por favor, selecciona una base de datos y al menos una tabla.", null);
            return;
        }

        try {
            tableView.populateTableFields(database, table1, table2, dbManager);
            tableView.getGoBackButton().setOnAction(event -> mainScene.setRoot(view.getLayout()));
            mainScene.setRoot(tableView.getLayout());
        } catch (SQLException e) {
            handleError("Error navegando a la vista de campos de tabla", e);
        }
    }

    private void navigateToResultView() throws SQLException {
        String query = queryBuilder.toString();

        if (query == null || query.isEmpty()) {
            handleError("Por favor genera un query antes de ver los resultados.", null);
            return;
        }

        ResultSet rs = dbManager.executeQuery(query);
        resultView.populateTable(rs);
        resultView.getGoBackButton().setOnAction(event -> mainScene.setRoot(view.getLayout()));

        mainScene.setRoot(resultView.getLayout());
    }

    private void executeQuery() throws SQLException {
        String query = queryBuilder.toString();
        if (query.isEmpty()) {
            handleError("Por favor genera un query válido antes de ejecutarlo.", null);
            return;
        }

        ResultSet rs = dbManager.executeQuery(query);
        resultView.populateTable(rs);
        view.getQueryTerminal().appendText("Query ejecutado correctamente.\n");
    }

    private void addConditionRow() {
        int conditionCount = view.getConditionSection().getChildren().size();
        if (conditionCount >= 2) {
            handleError("Solo se pueden agregar un máximo de 2 condiciones.", null);
            return;
        }

        HBox conditionRow = new HBox(10);

        ComboBox<String> fieldSelector = new ComboBox<>();
        fieldSelector.setItems(getAllFields());
        conditionRow.getChildren().add(fieldSelector);

        ComboBox<String> operatorSelector = new ComboBox<>();
        operatorSelector.setItems(FXCollections.observableArrayList("<", ">", "=", "<=", ">=", "<>", "LIKE", "NOT LIKE"));
        conditionRow.getChildren().add(operatorSelector);

        TextField valueField = new TextField();
        conditionRow.getChildren().add(valueField);

        view.getConditionSection().getChildren().add(conditionRow);

        fieldSelector.setOnAction(event -> updateQuery());
        operatorSelector.setOnAction(event -> updateQuery());
        valueField.setOnKeyReleased(event -> updateQuery());
    }

    private void resetConditions() {
        view.getConditionSection().getChildren().clear();
    }

    private void updateQuery() {
        String database = view.getDatabaseSelector().getValue();
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        queryBuilder.setLength(0); // Reiniciar query
        queryBuilder.append("SELECT * FROM ");

        if (table1 != null && !table1.isEmpty()) {
            queryBuilder.append(database).append(".").append(table1);
        }

        if (table2 != null && !table2.isEmpty()) {
            queryBuilder.append(", ").append(database).append(".").append(table2);
        }

        ObservableList<Node> conditions = view.getConditionSection().getChildren();
        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ");
            for (Node node : conditions) {
                if (node instanceof HBox) {
                    HBox condition = (HBox) node;
                    ComboBox<String> fieldSelector = (ComboBox<String>) condition.getChildren().get(0);
                    ComboBox<String> operatorSelector = (ComboBox<String>) condition.getChildren().get(1);
                    TextField valueField = (TextField) condition.getChildren().get(2);

                    String field = fieldSelector.getValue();
                    String operator = operatorSelector.getValue();
                    String value = valueField.getText();

                    if (field != null && operator != null && value != null && !value.isEmpty()) {
                        queryBuilder.append(field).append(" ").append(operator).append(" '").append(value).append("' AND ");
                    }
                }
            }

            // Eliminar el último " AND "
            int lastIndex = queryBuilder.lastIndexOf(" AND ");
            if (lastIndex != -1) {
                queryBuilder.delete(lastIndex, queryBuilder.length());
            }
        }

        view.getQueryTerminal().setText(queryBuilder.toString());
    }

    private ObservableList<String> getAllFields() {
        ObservableList<String> fields = FXCollections.observableArrayList();

        String database = view.getDatabaseSelector().getValue();
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        if (database != null && table1 != null) {
            List<String> columnsTable1 = dbManager.getColumnNames(database, table1);
            for (String column : columnsTable1) {
                fields.add(table1 + "." + column);
            }
        }
        if (database != null && table2 != null) {
            List<String> columnsTable2 = dbManager.getColumnNames(database, table2);
            for (String column : columnsTable2) {
                fields.add(table2 + "." + column);
            }
        }

        return fields;
    }

    private void handleError(String message, Exception e) {
        if (e != null) e.printStackTrace();
        view.getQueryTerminal().appendText(message + (e != null ? ": " + e.getMessage() : "") + "\n");
    }
}
