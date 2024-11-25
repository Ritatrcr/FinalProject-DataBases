package finalproject;

import controllers.ConnectionController;
import controllers.UserSelectionController;
import controllers.DashboardController;
import controllers.UserQueryController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.DatabaseManager;
import view.ConnectionView;
import view.UserSelectionView;
import view.DashboardView;
import view.UserQueryView;

public class FinalProject extends Application {

    private static Stage primaryStage;

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
     * Muestra la vista de selección inicial después de establecer conexión.
     *
     * @param dbManager Instancia de DatabaseManager.
     */
    public static void showUserSelectionScene(DatabaseManager dbManager) {
        UserSelectionView selectionView = new UserSelectionView();
        UserSelectionController userSelectionController = new UserSelectionController(selectionView, primaryStage, dbManager);

        Scene selectionScene = new Scene(selectionView.getLayout(), 900, 700);
        primaryStage.setScene(selectionScene);
        primaryStage.setTitle("Seleccionar Opción");
    }

    /**
     * Muestra el dashboard después de establecer la conexión a la base de datos.
     *
     * @param dbManager Instancia de DatabaseManager.
     */
    public static void showDashboardScene(DatabaseManager dbManager) {
        DashboardView dashboardView = new DashboardView();
        DashboardController dashboardController = new DashboardController(dashboardView, dbManager);
        dashboardController.setup();

        Scene dashboardScene = new Scene(dashboardView.getLayout(), 900, 700);
        primaryStage.setScene(dashboardScene);
        primaryStage.setTitle("Dashboard de Gestión de Base de Datos");
    }

    /**
     * Muestra la vista de consulta dinámica (UserQuery).
     *
     * @param dbManager Instancia de DatabaseManager.
     */
    public static void showUserQueryScene(DatabaseManager dbManager) {
        UserQueryView userQueryView = new UserQueryView();
        UserQueryController userQueryController = new UserQueryController(userQueryView, dbManager, primaryStage.getScene());
        // Configurar el controlador
        userQueryController.setup();

        Scene userQueryScene = new Scene(userQueryView.getLayout(), 900, 700);
        primaryStage.setScene(userQueryScene);
        primaryStage.setTitle("Consulta Dinámica");
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
