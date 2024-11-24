package view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Vista para mostrar el resultado de una consulta SQL.
 */
public class ResultView {
    private BorderPane layout;
    private TableView<ObservableList<String>> resultTable;
    private Button goBackButton;
    private TextArea queryTerminal; // Añadido: Campo para la terminal de consulta

    public ResultView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Sección central: tabla de resultados
        resultTable = new TableView<>();
        layout.setCenter(resultTable);

        // Sección inferior: botón para volver y terminal de consulta
        VBox bottomSection = new VBox(10);
        bottomSection.setPadding(new Insets(10));

        goBackButton = new Button("Volver");

        // Añadido: Inicializar y configurar el TextArea de la terminal
        queryTerminal = new TextArea();
        queryTerminal.setEditable(false);
        queryTerminal.setPrefHeight(100);
        queryTerminal.setPromptText("Mensajes de consulta...");

        bottomSection.getChildren().addAll(goBackButton, queryTerminal);
        layout.setBottom(bottomSection);
    }

    /**
     * Devuelve el layout principal de la vista.
     *
     * @return El layout principal.
     */
    public BorderPane getLayout() {
        return layout;
    }

    /**
     * Devuelve la tabla de resultados.
     *
     * @return El TableView de resultados.
     */
    public TableView<ObservableList<String>> getResultTable() {
        return resultTable;
    }

    /**
     * Devuelve el botón "Volver".
     *
     * @return El botón de "Volver".
     */
    public Button getGoBackButton() {
        return goBackButton;
    }

    /**
     * Llena la tabla de resultados con los datos de un ResultSet.
     *
     * @param resultSet El ResultSet con los datos a mostrar.
     */
    public void populateTable(java.sql.ResultSet resultSet) {
        resultTable.getColumns().clear();
        resultTable.getItems().clear();

        try {
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Crear columnas dinámicamente
            for (int i = 1; i <= columnCount; i++) {
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i));
                final int colIndex = i - 1;
                column.setCellValueFactory(param -> {
                    ObservableList<String> row = param.getValue();
                    return (row != null && colIndex < row.size())
                            ? new javafx.beans.property.SimpleStringProperty(row.get(colIndex))
                            : new javafx.beans.property.SimpleStringProperty("");
                });
                resultTable.getColumns().add(column);
            }

            // Agregar filas
            while (resultSet.next()) {
                ObservableList<String> row = javafx.collections.FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                resultTable.getItems().add(row);
            }

            // Añadir mensaje a la terminal si se desea
            queryTerminal.appendText("Resultados cargados correctamente.\n");

        } catch (Exception e) {
            e.printStackTrace();
            queryTerminal.appendText("Error al cargar los resultados: " + e.getMessage() + "\n");
        }
    }

    /**
     * Devuelve el área de texto que funciona como terminal para consultas.
     *
     * @return El TextArea de la terminal.
     */
    public TextArea getQueryTerminal() {
        return queryTerminal;
    }
}
