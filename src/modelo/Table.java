/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Representa una tabla dentro de una base de datos.
 */
public class Table {
    private String name;

    public Table(String name) {
        this.name = name;
    }

    /**
     * Obtiene el nombre de la tabla.
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la tabla.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

