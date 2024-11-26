package controllers;

import finalproject.FinalProject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import modelo.DatabaseManager;
import view.FieldAlias;
import view.UserQueryView;
import view.TableFieldsView;
import view.ResultView;
import view.DashboardView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.TextArea;

public class UserQueryController {
    private UserQueryView view;
    private DatabaseManager dbManager;
    private TableFieldsView tableView;
    private ResultView resultView;
    private DashboardView dashboardview;
    private Scene mainScene;
    private String query; // Almacena el query completo
    private TextArea sharedQueryTerminal;


    public UserQueryController(UserQueryView view, DatabaseManager dbManager, Scene mainScene) {
        this.view = view;
        this.dbManager = dbManager;
        this.mainScene = mainScene;
        this.tableView = new TableFieldsView(view.getQueryTerminal()); // Pasar la terminal compartida
        this.resultView = new ResultView();
        this.dashboardview = new DashboardView();
        this.query = "";
       
        
       // Inicializamos con la terminal de UserQueryView

        setup();
    }

    public void setup() {
        loadDatabases();
        view.getDatabaseSelector().setOnAction(event -> loadTables());
        view.getFirstTableSelector().setOnAction(event -> {
            displaySelectedTablesStructure();
            resetConditions();
            updateQuery();
        });
        view.getSecondTableSelector().setOnAction(event -> {
            displaySelectedTablesStructure();
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
        
        view.getAddConditionButton().setOnAction(event -> addConditionRow());
        view.getGoBackButton().setOnAction(event -> navigateToDashboard());
        view.getClearButton().setOnAction(event -> view.clearSelections());

    }
    //carga las bases de datos
    private void loadDatabases() {
        List<String> databases = dbManager.getDatabaseNames();
        if (!databases.isEmpty()) {
            ObservableList<String> observableDatabases = FXCollections.observableArrayList(databases);
            view.getDatabaseSelector().setItems(observableDatabases);
        }
    }
//carga las tablas
    private void loadTables() {
        String database = view.getDatabaseSelector().getValue();
        if (database == null) return;

        List<String> tables = dbManager.getTableNames(database);
        ObservableList<String> observableTables = FXCollections.observableArrayList(tables);
        view.getFirstTableSelector().setItems(observableTables);
        view.getSecondTableSelector().setItems(observableTables);
    }
//muestra la estructura
    private void displaySelectedTablesStructure() {
        String database = view.getDatabaseSelector().getValue();
        if (database == null || database.isEmpty()) {
            return;
        }
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        StringBuilder sb = new StringBuilder();

        try {
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

            

        } catch (SQLException e) {
            handleError("Error obteniendo estructura de las tablas", e);
        }
    }
//navegar a la vista de las tablas con todos sus campos
    private void navigateToTableFieldsView() {
        String database = view.getDatabaseSelector().getValue();
        String table1 = view.getFirstTableSelector().getValue();
        String table2 = view.getSecondTableSelector().getValue();

        if (database == null || table1 == null) {
            handleError("Por favor, selecciona una base de datos y al menos una tabla.", null);
            return;
        }
        tableView.setDatabaseName(database);
        ObservableList<FieldAlias> table1Fields = FXCollections.observableArrayList();
        List<String> columnsTable1 = dbManager.getColumnNames(database, table1);
        for (String column : columnsTable1) {
            table1Fields.add(new FieldAlias(table1 + "." + column));
        }
        ObservableList<FieldAlias> table2Fields = FXCollections.observableArrayList();
        if (table2 != null && !table2.isEmpty()) {
            List<String> columnsTable2 = dbManager.getColumnNames(database, table2);
            for (String column : columnsTable2) {
                table2Fields.add(new FieldAlias(table2 + "." + column));
            }
        }
        tableView.populateTableFields(table1Fields, table2Fields);
        tableView.getGoBackButton().setOnAction(event -> {
            // Obtener el query construido en TableFieldsView
            String partialQuery = tableView.buildQuery();
            
            // Añadir condiciones adicionales
            StringBuilder queryBuilder = new StringBuilder(partialQuery);
            addAdditionalConditions(queryBuilder);
            
            // Actualizar el query completo
            query = queryBuilder.toString();
            
            // Mostrar el query en la vista principal
            view.getQueryTerminal().setText(query);
            
            // Regresar a la vista principal
            mainScene.setRoot(view.getLayout());
        });
        mainScene.setRoot(tableView.getLayout());
    }
//adicionar otras condiciones al query
    private void addAdditionalConditions(StringBuilder queryBuilder) {
        ObservableList<Node> conditions = view.getConditionSection().getChildren();
        if (!conditions.isEmpty()) {
            if (!queryBuilder.toString().contains("WHERE")) {
                queryBuilder.append(" WHERE ");
            } else {
                queryBuilder.append(" AND ");
            }
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
    }

    private void executeQuery() throws SQLException {
        if (query.isEmpty()) {
            handleError("Por favor genera un query válido antes de ejecutarlo.", null);
            return;
        }

        ResultSet rs = dbManager.executeQuery(query);
        resultView.populateTable(rs);
        resultView.getQueryTerminal().appendText("Query ejecutado correctamente.\n");
    }

    private void navigateToResultView() throws SQLException {
    if (query == null || query.isEmpty()) {
        handleError("Por favor genera un query antes de ver los resultados.", null);
        return;
    }

    ResultSet rs = dbManager.executeQuery(query);
    resultView.populateTable(rs);
    resultView.getQueryTerminal().setText(query); // Mostrar el query en la terminal

    String selectedDatabase = view.getDatabaseSelector().getValue(); // Obtener la base de datos seleccionada

    // Crear y configurar el controlador para la vista de resultados
    ResultViewController resultViewController = new ResultViewController(resultView, dbManager, query, selectedDatabase);

    // Configurar el botón "Volver"
    resultView.getGoBackButton().setOnAction(event -> mainScene.setRoot(view.getLayout()));

    mainScene.setRoot(resultView.getLayout());
}

   


//añadir estructura de las condiciones
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

    private void updateQuery() {
        // Re-construir el query basado en los alias y condiciones
        String partialQuery = tableView.buildQuery();
        StringBuilder queryBuilder = new StringBuilder(partialQuery);
        addAdditionalConditions(queryBuilder);

        query = queryBuilder.toString();

        // Actualizar el query en la vista principal
        view.getQueryTerminal().setText(query);
    }

    private void resetConditions() {
        view.getConditionSection().getChildren().clear();
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
    private void navigateToDashboard() {
    try {
        // Llamar a FinalProject para gestionar la navegación al DashboardView
        FinalProject.showDashboardScene(dbManager);
        System.out.println("Navegando de UserQueryView a DashboardView...");
    } catch (Exception e) {
        handleError("Error navegando al DashboardView.", e);
    }
}


    private void handleError(String message, Exception e) {
        if (e != null) e.printStackTrace();
        view.getQueryTerminal().appendText(message + (e != null ? ": " + e.getMessage() : "") + "\n");
    }
}
