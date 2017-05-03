/*
 * Clase modelo para generar los datos de las comisiones
 * Creado: 22 de febrero del 2017
 * Por: Rigo Galicia
 */
package com.analitycs.modelo;

import java.util.ArrayList;

public class DatosComision {
    // Declaracion de campos de la clase
    private int idAgencia;
    private int operador;
    private String nombre;
    private ArrayList<DetalleComision> detalleComision;
    
    // Declaracion de metodos establecer y obtener
    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    public int getOperador() {
        return operador;
    }

    public void setOperador(int operador) {
        this.operador = operador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<DetalleComision> getDetalleComision() {
        return detalleComision;
    }

    public void setDetalleComision(ArrayList<DetalleComision> detalleComision) {
        this.detalleComision = detalleComision;
    }

}
