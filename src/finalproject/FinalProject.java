package finalproject;

import controllers.ConnectionController;
import controllers.SelectDatabaseController;
import controllers.TableSelectionController;
import modelo.DatabaseManager;
import view.ConnectionView;
import view.DatabaseSelectionView;
import view.TableSelectionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que inicia la aplicación y maneja la navegación entre escenas.
 */
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

    /**
     * Muestra la escena de conexión a la base de datos.
     */
    public static void showConnectionScene() {
        // Crear la vista y el controlador
        ConnectionView view = new ConnectionView();
        ConnectionController controller = new ConnectionController(view);

        // Configurar las acciones del controlador
        controller.setup();

        // Crear la escena y asignarla al stage
        Scene scene = new Scene(view.getLayout(), 500, 400);
        primaryStage.setScene(scene);
    }

    /**
     * Muestra la escena de selección de bases de datos.
     */
    public static void showDatabaseSelectionScene() {
        // Crear la vista y el controlador
        DatabaseSelectionView view = new DatabaseSelectionView();
        SelectDatabaseController controller = new SelectDatabaseController(view);

        // Configurar las acciones del controlador
        controller.setup();

        // Crear la escena y asignarla al stage
        Scene scene = new Scene(view.getLayout(), 400, 300);
        primaryStage.setScene(scene);
    }

    /**
     * Muestra la escena de selección de tablas.
     */
    public static void showTableSelectionScene() {
        // Crear la vista y el controlador
        TableSelectionView view = new TableSelectionView();
        TableSelectionController controller = new TableSelectionController(view);

        // Configurar las acciones del controlador
        controller.setup();

        // Crear la escena y asignarla al stage
        Scene scene = new Scene(view.getLayout(), 400, 300);
        primaryStage.setScene(scene);
    }

    // Métodos para manejar la base de datos seleccionada
    public static void setSelectedDatabase(String database) {
        selectedDatabase = database;
    }

    public static String getSelectedDatabase() {
        return selectedDatabase;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Cerrar la conexión a la base de datos al detener la aplicación
        if (DatabaseManager.getInstance() != null) {
            DatabaseManager.getInstance().closeConnection();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
