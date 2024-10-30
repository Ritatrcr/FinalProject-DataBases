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
import java.sql.SQLException;

public class FinalProject extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conectar a Base de Datos");

        // Etiqueta y campo para ingresar la IP del servidor
        Label labelIP = new Label("IP del Servidor:");
        TextField textFieldIP = new TextField("localhost");  // Valor predeterminado "localhost"

        // Etiqueta y campo para ingresar el puerto de conexión
        Label labelPort = new Label("Puerto:");
        TextField textFieldPort = new TextField("3306");     // Puerto predeterminado 3306 para MySQL

        // Etiqueta y campo para ingresar el nombre de usuario
        Label labelUsername = new Label("Usuario:");
        TextField textFieldUsername = new TextField("root"); // Nombre de usuario predeterminado "root"

        // Etiqueta y campo para ingresar la contraseña (se muestra en texto claro)
        Label labelPassword = new Label("Contraseña:");
        TextField passwordField = new TextField(); // Utiliza TextField en lugar de PasswordField para mostrar la contraseña

        // Botón para iniciar la conexión
        Button connectButton = new Button("Conectar");

        // Botón "Siguiente" que puede usarse para navegar después de una conexión exitosa
        Button nextButton = new Button("Siguiente");
        nextButton.setDisable(true); // Desactivado inicialmente hasta que la conexión sea exitosa

        // TextArea para simular la terminal
        TextArea terminalOutput = new TextArea();
        terminalOutput.setEditable(false); // No se puede editar
        terminalOutput.setWrapText(true);  // Ajustar texto a la línea
        terminalOutput.setPrefHeight(100); // Ajustar el tamaño del cuadro
        terminalOutput.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; -fx-highlight-fill: white; -fx-highlight-text-fill: black; -fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Acción del botón de conexión
        connectButton.setOnAction(event -> {
            // Obtener valores de los campos de texto
            String ip = textFieldIP.getText();
            String port = textFieldPort.getText();
            String username = textFieldUsername.getText();
            String password = passwordField.getText();

            // Crear y mostrar el comando de conexión en el cuadro de terminal
            String command = "jdbc:mysql://" + ip + ":" + port;
            terminalOutput.appendText("Trying to connect to: " + command + "\n");

            // Intentar conectar a la base de datos y mostrar el resultado en el cuadro de terminal
            if (connectToDatabase(ip, port, username, password, terminalOutput)) {
                terminalOutput.appendText("Conexión exitosa\n");
                nextButton.setDisable(false); // Habilitar el botón "Siguiente" si la conexión es exitosa
            } else {
                terminalOutput.appendText("Error en la conexión\n");
            }
        });

        // Disposición de los componentes de la interfaz en una cuadrícula
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10)); // Espacio alrededor del grid
        grid.setVgap(8);  // Espacio vertical entre componentes
        grid.setHgap(10); // Espacio horizontal entre componentes

        // Añadir componentes al grid
        grid.add(labelIP, 0, 0);
        grid.add(textFieldIP, 1, 0);
        grid.add(labelPort, 0, 1);
        grid.add(textFieldPort, 1, 1);
        grid.add(labelUsername, 0, 2);
        grid.add(textFieldUsername, 1, 2);
        grid.add(labelPassword, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(connectButton, 1, 4);

        // Configuración del botón "Siguiente" en la esquina inferior derecha
        HBox bottomRightBox = new HBox(nextButton);
        bottomRightBox.setAlignment(Pos.BOTTOM_RIGHT); // Alineación a la derecha
        bottomRightBox.setPadding(new Insets(10)); // Espacio alrededor

        // Configuración de BorderPane para organizar la interfaz
        BorderPane layout = new BorderPane();
        layout.setCenter(grid);               // Grid en el centro
        layout.setBottom(terminalOutput);      // Terminal en la parte inferior
        layout.setRight(bottomRightBox);       // Botón "Siguiente" en la parte inferior derecha

        // Configurar la escena principal
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para la conexión a la base de datos
    private boolean connectToDatabase(String ip, String port, String username, String password, TextArea terminalOutput) {
        // Crear URL de conexión a MySQL
        String url = "jdbc:mysql://" + ip + ":" + port ; // Se conecta a la base de datos "world"
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Si la conexión es exitosa, mostrar mensaje en la terminal
            terminalOutput.appendText("Conexión exitosa a la base de datos.\n");
            return true;
        } catch (SQLException e) {
            // Si ocurre un error, mostrar el mensaje de error en la terminal
            terminalOutput.appendText("Error en la conexión: " + e.getMessage() + "\n");
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args); // Iniciar la aplicación JavaFX
    }
}
