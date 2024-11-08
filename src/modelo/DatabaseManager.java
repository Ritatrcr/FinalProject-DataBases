/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javafx.scene.control.TextArea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Singleton para manejar la conexión a la base de datos.
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private String ip;
    private String port;
    private String username;
    private String password;
    private Connection connection;
    private TextArea terminalOutput;

    // Constructor privado para Singleton
    private DatabaseManager(String ip, String port, String username, String password, TextArea terminalOutput) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.terminalOutput = terminalOutput;
    }

    /**
     * Inicializa la instancia de DatabaseManager si no existe.
     */
    public static DatabaseManager getInstance(String ip, String port, String username, String password, TextArea terminalOutput) {
        if (instance == null) {
            instance = new DatabaseManager(ip, port, username, password, terminalOutput);
        }
        return instance;
    }

    /**
     * Obtiene la instancia existente de DatabaseManager.
     */
    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Conecta a la base de datos utilizando los parámetros proporcionados.
     */
    public boolean connect() {
        // Cerrar la conexión anterior si existe
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    terminalOutput.appendText("Conexión anterior cerrada.\n");
                }
            } catch (SQLException e) {
                terminalOutput.appendText("Error al cerrar la conexión anterior: " + e.getMessage() + "\n");
            }
        }

        String url = "jdbc:mysql://" + ip + ":" + port;
        try {
            connection = DriverManager.getConnection(url, username, password);
            terminalOutput.appendText("Conexión exitosa a la base de datos.\n");
            return true;
        } catch (SQLException e) {
            terminalOutput.appendText("Error en la conexión: " + e.getMessage() + "\n");
            return false;
        }
    }

    /**
     * Obtiene las bases de datos disponibles.
     */
    public ResultSet getDatabases() throws SQLException {
        String query = "SHOW DATABASES";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Obtiene las tablas de una base de datos específica.
     */
    public ResultSet getTables(String databaseName) throws SQLException {
        String query = "SHOW TABLES FROM " + databaseName;
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                terminalOutput.appendText("Conexión cerrada.\n");
            }
        } catch (SQLException e) {
            terminalOutput.appendText("Error al cerrar la conexión: " + e.getMessage() + "\n");
        }
    }

    public void executeCustomQuery(String toString, TextArea terminalOutput) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResultSet getFields(String table) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResultSet getColumns(String table) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    public ResultSet getColumnsDetails(String tableName) throws SQLException {
    String query = "SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT, EXTRA " +
                   "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'";
    Statement stmt = connection.createStatement();
    return stmt.executeQuery(query);
    }
    
    public ObservableList<ColumnDetails> getColumnsDetailsAsObservableList(String tableName) {
        ObservableList<ColumnDetails> columnsData = FXCollections.observableArrayList();
        String query = "SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT, EXTRA " +
                       "FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName + "'";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String field = rs.getString("COLUMN_NAME");
                String type = rs.getString("COLUMN_TYPE");
                String nullable = rs.getString("IS_NULLABLE");
                String key = rs.getString("COLUMN_KEY");
                String defaultValue = rs.getString("COLUMN_DEFAULT");
                String extra = rs.getString("EXTRA");

                columnsData.add(new ColumnDetails(field, type, nullable, key, defaultValue, extra));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnsData;
    }

}
