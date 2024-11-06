/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Representa una base de datos.
 */
public class Database {
    private String name;

    public Database(String name) {
        this.name = name;
    }

    /**
     * Obtiene el nombre de la base de datos.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la base de datos.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
