package finalproject;

import controllers.ConnectionController;
import controllers.UserSelectionController;
import controllers.DashboardController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.DatabaseManager;
import view.ConnectionView;
import view.UserSelectionView;
import view.DashboardView;

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
        UserSelectionController UserselectionController = new UserSelectionController(selectionView, primaryStage, dbManager);

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

        Scene dashboardScene = new Scene(dashboardView.getScrollPane(), 900, 700);
        primaryStage.setScene(dashboardScene);
        primaryStage.setTitle("Dashboard de Gestión de Base de Datos");
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
