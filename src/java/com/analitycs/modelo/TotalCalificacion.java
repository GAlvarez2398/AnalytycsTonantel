/*
 * Clase POJO para almacenar el total de la calificacion del servicio al asociado
 */
package com.analitycs.modelo;

public class TotalCalificacion {
    // Declaracion de campos de la clase
    private int excelente;
    private int bueno;
    private int regular;
    private int malo;
    
    // Declaracion de metodos establecer y obtener
    public int getExcelente() {
        return excelente;
    }

    public void setExcelente(int excelente) {
        this.excelente = excelente;
    }

    public int getBueno() {
        return bueno;
    }

    public void setBueno(int bueno) {
        this.bueno = bueno;
    }

    public int getRegular() {
        return regular;
    }

    public void setRegular(int regular) {
        this.regular = regular;
    }

    public int getMalo() {
        return malo;
    }

    public void setMalo(int malo) {
        this.malo = malo;
    }
    
    
}
