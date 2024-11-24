package controllers;

import modelo.DatabaseManager;
import view.TableFieldsView;

public class TableViewController {
    private TableFieldsView view;
    private DatabaseManager dbManager;

    public TableViewController(TableFieldsView view, DatabaseManager dbManager, UserQueryController aThis) {
        this.view = view;
        this.dbManager = dbManager;
    }
}
