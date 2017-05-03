/*
 * Clase modelo para estructurar el detalle de las comisiones
 * Creado: 22 de febrero del 2018
 * Por: Rigo Galicia
 */
package com.analitycs.modelo;


public class DetalleComision {
    // Declaracion de campos
    private String producto;
    private int cantidad;
    private double comision;
    private double total;
    
    // Declaracion de metodos establecer y obtener
    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
}
