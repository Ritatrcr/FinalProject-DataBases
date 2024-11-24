package controllers;

import finalproject.FinalProject;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.DatabaseManager;
import view.UserQueryView;
import view.UserSelectionView;

/**
 * Controlador para la vista de selección inicial del usuario.
 */
public class UserSelectionController {
    private UserSelectionView view;
    private Stage primaryStage;
    private DatabaseManager dbManager;

    /**
     * Constructor para el controlador de UserSelection.
     *
     * @param view Vista de selección inicial.
     * @param primaryStage Escenario principal de la aplicación.
     * @param dbManager Instancia del gestor de bases de datos.
     */
    public UserSelectionController(UserSelectionView view, Stage primaryStage, DatabaseManager dbManager) {
        this.view = view;
        this.primaryStage = primaryStage;
        this.dbManager = dbManager;

        // Configurar acciones de los botones
        configureActions();
    }

    /**
     * Configura las acciones para los botones en la vista.
     */
    private void configureActions() {
        // Acción para consultar registros
        view.getConsultarRegistrosButton().setOnAction(event -> {
            // Cambiar a la vista del dashboard
            FinalProject.showDashboardScene(dbManager);
        });

        // Acción para realizar queries
        view.getRealizarQueryButton().setOnAction(event -> {
            // Crear la vista de consultas
            UserQueryView queryView = new UserQueryView();

            // Crear la escena de consultas
            Scene queryScene = new Scene(queryView.getLayout(), 900, 700);

            // Crear el controlador de consultas, pasando la escena
            UserQueryController queryController = new UserQueryController(queryView, dbManager, queryScene);

            // Mostrar la escena de consultas
            primaryStage.setScene(queryScene);
            primaryStage.setTitle("Realizar Query");
        });
    }
}
