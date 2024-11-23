/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import finalproject.FinalProject;
import javafx.stage.Stage;
import modelo.DatabaseManager;
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
            // Aquí se redirigiría a la vista de queries (por implementar)
            System.out.println("Realizar Query seleccionado");
        });
    }
}
