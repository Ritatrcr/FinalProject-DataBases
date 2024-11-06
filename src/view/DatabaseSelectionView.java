package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista para la selección de bases de datos.
 */
public class DatabaseSelectionView {
    private BorderPane layout;
    private Button previousButton;
    private Button nextButton;
    private TextArea terminalOutput;
    private ScrollPane scrollPane;
    private VBox databasesBox;
    private Label instructionLabel;

    public DatabaseSelectionView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Parte superior: Instrucción clara y visible
        instructionLabel = new Label("Seleccione una base de datos de la lista a continuación:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        instructionLabel.setWrapText(true); // Permite que el texto se ajuste a múltiples líneas si es necesario
        HBox topBox = new HBox(instructionLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        layout.setTop(topBox);

        // Centro: ScrollPane con VBox que contiene los CheckBoxes alineados a la izquierda y ordenados
        databasesBox = new VBox(10);
        databasesBox.setAlignment(Pos.TOP_LEFT);
        databasesBox.setPadding(new Insets(10));

        scrollPane = new ScrollPane(databasesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300); // Aumenta la altura para una mejor visibilidad
        scrollPane.setStyle("-fx-border-color: gray; -fx-border-width: 2px;");

        layout.setCenter(scrollPane);

        // Parte inferior: TextArea para terminal
        terminalOutput = new TextArea();
        terminalOutput.setEditable(false);
        terminalOutput.setWrapText(true);
        terminalOutput.setPrefHeight(150);
        terminalOutput.setStyle("-fx-control-inner-background: black; -fx-font-family: monospace; " +
                "-fx-highlight-fill: white; -fx-highlight-text-fill: black; " +
                "-fx-text-fill: white; -fx-border-color: gray; -fx-border-width: 2px; " +
                "-fx-border-radius: 5px;");
        layout.setBottom(terminalOutput);

        // Botones "Anterior" y "Siguiente" en la parte inferior
        previousButton = new Button("Anterior");
        nextButton = new Button("Siguiente");
        nextButton.setDisable(true); // Inicialmente deshabilitado

        HBox bottomBox = new HBox(10, previousButton, nextButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT); // Alinea los botones a la derecha
        bottomBox.setPadding(new Insets(10));
        layout.setBottom(bottomBox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public VBox getDatabasesBox() {
        return databasesBox;
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextButton() {
        return nextButton;
    }

    public TextArea getTerminalOutput() {
        return terminalOutput;
    }

    public Label getInstructionLabel() {
        return instructionLabel;
    }
}
