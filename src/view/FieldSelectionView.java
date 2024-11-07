package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class FieldSelectionView {
    private BorderPane layout;
    private VBox fieldsBox;
    private Button confirmButton;
    private TextArea terminalOutput;

    public FieldSelectionView(List<String> fields) {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Instrucción en la parte superior
        Label instructionLabel = new Label("Seleccione los campos a mostrar y asigne alias opcionales:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        layout.setTop(instructionLabel);

        // Centro: ScrollPane con VBox para los campos y alias
        fieldsBox = new VBox(10);
        for (String field : fields) {
            HBox fieldRow = new HBox(10);
            fieldRow.setAlignment(Pos.CENTER_LEFT);
            
            CheckBox fieldCheckBox = new CheckBox(field);
            TextField aliasField = new TextField();
            aliasField.setPromptText("Alias (opcional)");

            fieldRow.getChildren().addAll(fieldCheckBox, aliasField);
            fieldsBox.getChildren().add(fieldRow);
        }

        ScrollPane scrollPane = new ScrollPane(fieldsBox);
        scrollPane.setFitToWidth(true);
        layout.setCenter(scrollPane);

        // Botón de confirmación en la parte inferior
        confirmButton = new Button("Confirmar");
        layout.setBottom(confirmButton);

        // Terminal de salida en la parte inferior
        terminalOutput = new TextArea();
        terminalOutput.setEditable(false);
        layout.setBottom(terminalOutput);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public VBox getFieldsBox() {
        return fieldsBox;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public TextArea getTerminalOutput() {
        return terminalOutput;
    }

    public Iterable<HBox> getFieldRows() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
