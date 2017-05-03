/*
 * Clase POJO de producto
 */
package com.analitycs.modelo;

import java.math.BigDecimal;

public class Producto {
    // Declaracion de campos de la clase
    private String nombreProducto;
    private int cantidad;
    private BigDecimal monto;
    
    // Declaracion de metodos establecer y obtener
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
}
