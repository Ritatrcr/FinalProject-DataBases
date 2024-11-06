package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista para la selección de tablas.
 */
public class TableSelectionView {
    private BorderPane layout;
    private Button previousButton;
    private Button nextTableButton;
    private TextArea terminalOutput;
    private VBox tablesBox;
    private Label instructionLabel;

    public TableSelectionView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Parte superior: Instrucción clara y visible
        instructionLabel = new Label("Seleccione 1 o 2 tablas por medio del checkbox:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        instructionLabel.setWrapText(true); // Permite que el texto se ajuste a múltiples líneas si es necesario
        HBox topBox = new HBox(instructionLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        layout.setTop(topBox);

        // Centro: VBox con CheckBoxes alineados a la izquierda y ordenados
        tablesBox = new VBox(10);
        tablesBox.setAlignment(Pos.TOP_LEFT);
        tablesBox.setPadding(new Insets(10));

        layout.setCenter(tablesBox);

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
        nextTableButton = new Button("Siguiente");
        nextTableButton.setDisable(true); // Inicialmente deshabilitado

        HBox bottomBox = new HBox(10, previousButton, nextTableButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT); // Alinea los botones a la derecha
        bottomBox.setPadding(new Insets(10));
        layout.setBottom(bottomBox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public VBox getTablesBox() {
        return tablesBox;
    }

    public Button getPreviousButton() {
        return previousButton;
    }

    public Button getNextTableButton() {
        return nextTableButton;
    }

    public TextArea getTerminalOutput() {
        return terminalOutput;
    }

    public Label getInstructionLabel() {
        return instructionLabel;
    }
}
