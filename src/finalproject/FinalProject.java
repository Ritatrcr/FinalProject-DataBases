package finalproject;

import controllers.ColumnSlectionController;
import controllers.ConnectionController;
import controllers.SelectDatabaseController;
import controllers.TableSelectionController;
import modelo.DatabaseManager;
import view.ColumnSelectionView;
import view.ConnectionView;
import view.DatabaseSelectionView;
import view.TableSelectionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class FinalProject extends Application {

    private static Stage primaryStage;
    private static String selectedDatabase;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Gestor de Bases de Datos");

        // Mostrar la primera escena: Conexión
        showConnectionScene();

        primaryStage.show();
    }

    public static void showConnectionScene() {
        ConnectionView view = new ConnectionView();
        ConnectionController controller = new ConnectionController(view);
        controller.setup();
        Scene scene = new Scene(view.getLayout(), 900, 700);
        primaryStage.setScene(scene);
    }

    public static void showDatabaseSelectionScene() {
        DatabaseSelectionView view = new DatabaseSelectionView();
        SelectDatabaseController controller = new SelectDatabaseController(view);
        controller.setup();
        Scene scene = new Scene(view.getLayout(), 900, 700);
        primaryStage.setScene(scene);
    }

    public static void showTableSelectionScene() {
        TableSelectionView view = new TableSelectionView();
        TableSelectionController controller = new TableSelectionController(view);
        controller.setup();
        Scene scene = new Scene(view.getLayout(), 900, 700);
        primaryStage.setScene(scene);
    }

    /**
     * Muestra la escena de selección de columnas después de que se han seleccionado las tablas.
     * @param selectedTables Arreglo de nombres de tablas seleccionadas.
     */
    public static void showColumnSelectionScene(String[] selectedTables) {
        ColumnSelectionView view = new ColumnSelectionView();
        ColumnSlectionController controller = new ColumnSlectionController(view);
        controller.setup(selectedTables);
        Scene scene = new Scene(view.getLayout(), 900, 700);
        primaryStage.setScene(scene);
    }

    public static void setSelectedDatabase(String database) {
        selectedDatabase = database;
    }

    public static String getSelectedDatabase() {
        return selectedDatabase;
    }
    

    @Override
    public void stop() throws Exception {
        super.stop();
        if (DatabaseManager.getInstance() != null) {
            DatabaseManager.getInstance().closeConnection();
        }
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
