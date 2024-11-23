package modelo;

import javafx.scene.control.TextArea;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la conexión y las operaciones con la base de datos.
 */
public class DatabaseManager {
    private static DatabaseManager instance; // Singleton
    private Connection connection;
    private String ip, port, username, password;
    private TextArea terminalOutput;

    /**
     * Constructor privado para el patrón Singleton.
     */
    private DatabaseManager(String ip, String port, String username, String password, TextArea terminalOutput) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.terminalOutput = terminalOutput;
    }

    /**
     * Obtiene la instancia única de DatabaseManager.
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
     * Conecta a la base de datos usando los parámetros proporcionados.
     */
    public boolean connect() {
        try {
            String url = "jdbc:mysql://" + ip + ":" + port + "/information_schema";
            connection = DriverManager.getConnection(url, username, password);
            terminalOutput.appendText("Conexión exitosa al servidor.\n");
            return true;
        } catch (SQLException e) {
            terminalOutput.appendText("Error de conexión: " + e.getMessage() + "\n");
            return false;
        }
    }

    /**
     * Obtiene una lista de las bases de datos disponibles en el servidor.
     */
    public List<String> getDatabaseNames() {
    List<String> databases = new ArrayList<>();
    try (Statement stmt = connection.createStatement()) {
        ResultSet rs = stmt.executeQuery("SHOW DATABASES");
        while (rs.next()) {
            String dbName = rs.getString(1);
            System.out.println("Base de datos encontrada: " + dbName); // Log
            databases.add(dbName);
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log de errores
        System.out.println("Error al conectar");
    }
    return databases;
    
    
}
    
    public List<String> getColumnNames(String databaseName, String tableName) {
    List<String> columnNames = new ArrayList<>();
    String query = "SELECT * FROM " + databaseName + "." + tableName + " LIMIT 1"; // Consulta para obtener metadata

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i)); // Obtener nombre de la columna
        }
    } catch (SQLException e) {
        e.printStackTrace(); // O manejar el error de otra forma
    }

    return columnNames;
}



    /**
     * Obtiene una lista de las tablas de una base de datos específica.
     */
    public List<String> getTableNames(String databaseName) {
        List<String> tables = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SHOW TABLES FROM " + databaseName);
            while (rs.next()) {
                tables.add(rs.getString(1)); // Nombre de la tabla está en la primera columna
            }
        } catch (SQLException e) {
            terminalOutput.appendText("Error obteniendo tablas: " + e.getMessage() + "\n");
        }
        return tables;
    }

    /**
     * Obtiene los datos de una tabla específica dentro de una base de datos.
     */
    public ResultSet getTableData(String databaseName, String tableName) {
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery("SELECT * FROM " + databaseName + "." + tableName);
        } catch (SQLException e) {
            terminalOutput.appendText("Error obteniendo datos: " + e.getMessage() + "\n");
            return null;
        }
    }

    /**
     * Ejecuta un comando SQL para añadir, eliminar o modificar registros.
     */
    public boolean executeUpdate(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            terminalOutput.appendText("Comando ejecutado exitosamente.\n");
            return true;
        } catch (SQLException e) {
            terminalOutput.appendText("Error ejecutando comando: " + e.getMessage() + "\n");
            return false;
        }
    }
    
    /**
 * Inserta un nuevo registro en la tabla especificada.
 *
 * @param databaseName Nombre de la base de datos.
 * @param tableName Nombre de la tabla.
 * @param columnNames Lista con los nombres de las columnas.
 * @param values Lista con los valores correspondientes a las columnas.
 * @return true si la inserción fue exitosa, false en caso contrario.
 */
public boolean insertRecord(String databaseName, String tableName, List<String> columnNames, List<String> values) {
    // Validar que los parámetros no estén vacíos
    if (columnNames.isEmpty() || values.isEmpty() || columnNames.size() != values.size()) {
        terminalOutput.appendText("Error: Número de columnas y valores no coincide.\n");
        return false;
    }

    // Construir el comando SQL
    String columns = String.join(", ", columnNames);
    StringBuilder valuesBuilder = new StringBuilder();

    // Construir la lista de valores, reemplazando valores vacíos por NULL
    for (String value : values) {
        if (value == null || value.trim().isEmpty()) {
            valuesBuilder.append("NULL");
        } else {
            valuesBuilder.append("'").append(value.replace("'", "''")).append("'"); // Escapar comillas simples
        }
        valuesBuilder.append(", ");
    }

    // Eliminar la última coma y espacio extra
    String valuesPlaceholder = valuesBuilder.substring(0, valuesBuilder.length() - 2);

    String sql = "INSERT INTO " + databaseName + "." + tableName + " (" + columns + ") VALUES (" + valuesPlaceholder + ")";

    // Ejecutar el comando SQL
    try (Statement stmt = connection.createStatement()) {
        stmt.executeUpdate(sql);
        terminalOutput.appendText("Registro insertado exitosamente en " + tableName + ".\n");
        return true;
    } catch (SQLException e) {
        terminalOutput.appendText("Error insertando registro: " + e.getMessage() + "\n");
        return false;
    }
}
public List<String> getMandatoryFields(String databaseName, String tableName) {
    List<String> mandatoryFields = new ArrayList<>();
    String query = "SELECT COLUMN_NAME, IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS " +
                   "WHERE TABLE_SCHEMA = '" + databaseName + "' AND TABLE_NAME = '" + tableName + "'";

    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String isNullable = rs.getString("IS_NULLABLE");
            if ("NO".equalsIgnoreCase(isNullable)) { // Campo obligatorio
                mandatoryFields.add(columnName);
            }
        }
    } catch (SQLException e) {
        terminalOutput.appendText("Error obteniendo campos obligatorios: " + e.getMessage() + "\n");
    }

    return mandatoryFields;
}

    /**
     * Cierra la conexión a la base de datos.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                terminalOutput.appendText("Conexión cerrada correctamente.\n");
            }
        } catch (SQLException e) {
            terminalOutput.appendText("Error cerrando conexión: " + e.getMessage() + "\n");
        }
    }
}
