package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Vista para la conexión a la base de datos.
 */
public class ConnectionView {
    private BorderPane layout;
    private TextField textFieldIP;
    private TextField textFieldPort;
    private TextField textFieldUsername;
    private PasswordField passwordField;
    private Button connectButton;
    private Button nextButton;
    private TextArea terminalOutput;

    public ConnectionView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Parte superior: Texto de bienvenida
        Text welcomeText = new Text("Bienvenido a Gestor BD");
        welcomeText.setFont(Font.font(24));
        HBox topBox = new HBox(welcomeText);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        layout.setTop(topBox);

        // Centro: GridPane con campos de conexión
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Labels y campos
        Label labelIP = new Label("IP del Servidor:");
        textFieldIP = new TextField("localhost");
        textFieldIP.setPrefWidth(300);

        Label labelPort = new Label("Puerto:");
        textFieldPort = new TextField("3306");
        textFieldPort.setPrefWidth(300);

        Label labelUsername = new Label("Usuario:");
        textFieldUsername = new TextField("root");
        textFieldUsername.setPrefWidth(300);

        Label labelPassword = new Label("Contraseña:");
        passwordField = new PasswordField();
        passwordField.setPrefWidth(300);

        // Añadir componentes al GridPane
        grid.add(labelIP, 0, 0);
        grid.add(textFieldIP, 1, 0);
        grid.add(labelPort, 0, 1);
        grid.add(textFieldPort, 1, 1);
        grid.add(labelUsername, 0, 2);
        grid.add(textFieldUsername, 1, 2);
        grid.add(labelPassword, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label(), 0, 4); // Espacio vacío
        grid.add(connectButton = new Button("Conectar"), 1, 4);

        layout.setCenter(grid);

        // Parte inferior: TextArea para terminal
        terminalOutput = new TextArea();
        terminalOutput.setEditable(false);
        terminalOutput.setWrapText(true);
        terminalOutput.setPrefHeight(150);
        terminalOutput.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; " +
                "-fx-highlight-fill: white; -fx-highlight-text-fill: black; " +
                "-fx-text-fill: white; -fx-border-color: gray; " +
                "-fx-border-width: 2px; -fx-border-radius: 5px;");
        layout.setBottom(terminalOutput);

        // Botón "Siguiente" en la parte inferior derecha
        nextButton = new Button("Siguiente");
        nextButton.setDisable(true);
        HBox rightBox = new HBox(nextButton);
        rightBox.setAlignment(Pos.BOTTOM_RIGHT);
        rightBox.setPadding(new Insets(10));
        layout.setRight(rightBox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public TextField getTextFieldIP() {
        return textFieldIP;
    }

    public TextField getTextFieldPort() {
        return textFieldPort;
    }

    public TextField getTextFieldUsername() {
        return textFieldUsername;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public TextArea getTerminalOutput() {
        return terminalOutput;
    }
}
