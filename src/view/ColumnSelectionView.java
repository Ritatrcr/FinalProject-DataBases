package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ColumnSelectionView {
    private BorderPane layout;
    private VBox tablesBox;
    private Button continueButton;

    public ColumnSelectionView() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Instrucción para el usuario
        Label instructionLabel = new Label("Seleccione columnas y asigne un alias:");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox topBox = new HBox(instructionLabel);
        topBox.setAlignment(Pos.CENTER_LEFT);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        layout.setTop(topBox);

        // Caja central para tablas y columnas
        tablesBox = new VBox(10);
        tablesBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tablesBox);
        scrollPane.setFitToWidth(true);
        layout.setCenter(scrollPane);

        // Botón de continuar
        continueButton = new Button("Continuar");
        HBox bottomBox = new HBox(continueButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));
        layout.setBottom(bottomBox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    public VBox getTablesBox() {
        return tablesBox;
    }

    public Button getContinueButton() {
        return continueButton;
    }
}
