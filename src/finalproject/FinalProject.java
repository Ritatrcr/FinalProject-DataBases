package finalproject;

import controllers.ConnectionController;
import controllers.DashboardController;
import modelo.DatabaseManager;
import view.ConnectionView;
import view.DashboardView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinalProject extends Application {

    private static Stage primaryStage; // Ventana principal

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
        ConnectionView connectionView = new ConnectionView();
        ConnectionController connectionController = new ConnectionController(connectionView);
        connectionController.setup();

        Scene connectionScene = new Scene(connectionView.getLayout(), 900, 700);
        primaryStage.setScene(connectionScene);
    }

    /**
     * Muestra el dashboard después de establecer la conexión a la base de datos.
     *
     * @param dbManager Instancia de DatabaseManager
     * @param databaseName Nombre de la base de datos seleccionada
     */
 public static void showDashboardScene(DatabaseManager dbManager) {
    DashboardView dashboardView = new DashboardView();
    DashboardController dashboardController = new DashboardController(dashboardView, dbManager);
    dashboardController.setup();

    // Crear la escena con el ScrollPane de la vista
    Scene dashboardScene = new Scene(dashboardView.getScrollPane(), 900, 700);
    primaryStage.setScene(dashboardScene); // Cambiar la escena
    primaryStage.setTitle("Dashboard de Gestión de Base de Datos"); // Opcional: Cambiar título
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
