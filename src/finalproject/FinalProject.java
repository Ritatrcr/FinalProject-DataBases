package finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FinalProject extends Application {

    private Stage primaryStage;
    private Scene connectionScene;
    private Scene databaseSelectionScene;
    private ComboBox<String> databaseComboBox;
    private TextArea terminalOutput;

    // Variables para almacenar los datos de conexión
    private String ip;
    private String port;
    private String username;
    private String password;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Conectar a Base de Datos");

        // Crear las escenas
        connectionScene = createConnectionScene();
        databaseSelectionScene = createDatabaseSelectionScene();

        // Mostrar la primera escena
        primaryStage.setScene(connectionScene);
        primaryStage.show();
    }

    private Scene createConnectionScene() {
        // Componentes de conexión
        Label labelIP = new Label("IP del Servidor:");
        TextField textFieldIP = new TextField("localhost");

        Label labelPort = new Label("Puerto:");
        TextField textFieldPort = new TextField("3306");

        Label labelUsername = new Label("Usuario:");
        TextField textFieldUsername = new TextField("root");

        Label labelPassword = new Label("Contraseña:");
        TextField passwordField = new TextField();

        Button connectButton = new Button("Conectar");
        Button nextButton = new Button("Siguiente");
        nextButton.setDisable(true);

        terminalOutput = new TextArea();
        terminalOutput.setEditable(false);
        terminalOutput.setWrapText(true);
        terminalOutput.setPrefHeight(100);
        terminalOutput.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; -fx-highlight-fill: white; -fx-highlight-text-fill: black; -fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Acción del botón de conexión
        connectButton.setOnAction(event -> {
            ip = textFieldIP.getText();
            port = textFieldPort.getText();
            username = textFieldUsername.getText();
            password = passwordField.getText();

            String command = "jdbc:mysql://" + ip + ":" + port;
            terminalOutput.appendText("Trying to connect to: " + command + "\n");

            if (connectToDatabase(ip, port, username, password)) {
                terminalOutput.appendText("Conexión exitosa\n");
                nextButton.setDisable(false);
            } else {
                terminalOutput.appendText("Error en la conexión\n");
            }
        });

        // Acción del botón "Siguiente"
        nextButton.setOnAction(event -> {
            primaryStage.setScene(databaseSelectionScene);
            loadDatabases(); // Llamar a la función de carga de bases de datos al pasar a la siguiente escena
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(labelIP, 0, 0);
        grid.add(textFieldIP, 1, 0);
        grid.add(labelPort, 0, 1);
        grid.add(textFieldPort, 1, 1);
        grid.add(labelUsername, 0, 2);
        grid.add(textFieldUsername, 1, 2);
        grid.add(labelPassword, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(connectButton, 1, 4);

        HBox bottomRightBox = new HBox(nextButton);
        bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomRightBox.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setCenter(grid);
        layout.setBottom(terminalOutput);
        layout.setRight(bottomRightBox);

        return new Scene(layout, 500, 400);
    }

    private Scene createDatabaseSelectionScene() {
        Label labelDatabases = new Label("Seleccionar Base de Datos:");
        databaseComboBox = new ComboBox<>();

        // Crear botones y sus eventos
        Button previousButton = new Button("Anterior");
        Button nextButton = new Button("Siguiente");
        nextButton.setDisable(true); // Desactivar hasta que se seleccione una base de datos

        // Evento para habilitar "Siguiente" al seleccionar una base de datos
        databaseComboBox.setOnAction(event -> nextButton.setDisable(databaseComboBox.getValue() == null));

        previousButton.setOnAction(event -> primaryStage.setScene(connectionScene));
        nextButton.setOnAction(event -> {
            String selectedDatabase = databaseComboBox.getValue();
            if (selectedDatabase != null) {
                terminalOutput.appendText("Base de datos seleccionada: " + selectedDatabase + "\n");
                // Aquí puedes añadir lógica para la siguiente acción.
            }
        });

        // Configuración de los botones en la parte inferior
        HBox buttonBox = new HBox(10, previousButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setMargin(previousButton, new Insets(0, 0, 0, 0));
        HBox.setMargin(nextButton, new Insets(0, 0, 0, 0));

        // Distribuir botones "Anterior" y "Siguiente" a izquierda y derecha
        BorderPane layout = new BorderPane();
        layout.setCenter(new VBox(10, labelDatabases, databaseComboBox));
        layout.setBottom(buttonBox);
        
        BorderPane.setAlignment(previousButton, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);

        return new Scene(layout, 400, 300);
    }

    private boolean connectToDatabase(String ip, String port, String username, String password) {
        String url = "jdbc:mysql://" + ip + ":" + port;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            terminalOutput.appendText("Conexión exitosa a la base de datos.\n");
            return true;
        } catch (SQLException e) {
            terminalOutput.appendText("Error en la conexión: " + e.getMessage() + "\n");
            return false;
        }
    }

    private void loadDatabases() {
        databaseComboBox.getItems().clear();

        String url = "jdbc:mysql://" + ip + ":" + port;
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SHOW DATABASES")) {

            while (rs.next()) {
                databaseComboBox.getItems().add(rs.getString(1));
            }
        } catch (SQLException e) {
            terminalOutput.appendText("Error al cargar bases de datos: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
