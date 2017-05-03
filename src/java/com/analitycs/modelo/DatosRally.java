/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analitycs.modelo;

import java.util.ArrayList;

/**
 *
 * @author Jorge Manuel alvarez Molina
 */
public class DatosRally {
    //declaracion de los campos
    private String nombreColaborador;
    private ArrayList<Rally_Colocar> rallycolocar;
    private int cantidadAsociados;
    private double MontoCreditos;
    private String estadoAsociados;
    private String estadoCreditos;
    private int metaAsociados;
    private double metaCreditos;
    
    //declaracion de los metodos para establecer y obtener

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    public void setNombreColaborador(String nombreColaborador) {
        this.nombreColaborador = nombreColaborador;
    }

    public int getCantidadAsociados() {
        return cantidadAsociados;
    }

    public void setCantidadAsociados(int cantidadAsociados) {
        this.cantidadAsociados = cantidadAsociados;
    }

    public double getMontoCreditos() {
        return MontoCreditos;
    }

    public void setMontoCreditos(double MontoCreditos) {
        this.MontoCreditos = MontoCreditos;
    }

    public String getEstadoAsociados() {
        return estadoAsociados;
    }

    public void setEstadoAsociados(String estadoAsociados) {
        this.estadoAsociados = estadoAsociados;
    }

    public String getEstadoCreditos() {
        return estadoCreditos;
    }

    public void setEstadoCreditos(String estadoCreditos) {
        this.estadoCreditos = estadoCreditos;
    }

    public int getMetaAsociados() {
        return metaAsociados;
    }

    public void setMetaAsociados(int metaAsociados) {
        this.metaAsociados = metaAsociados;
    }

    public double getMetaCreditos() {
        return metaCreditos;
    }

    public void setMetaCreditos(double metaCreditos) {
        this.metaCreditos = metaCreditos;
    }

    public ArrayList<Rally_Colocar> getRallycolocar() {
        return rallycolocar;
    }

    public void setRallycolocar(ArrayList<Rally_Colocar> rallycolocar) {
        this.rallycolocar = rallycolocar;
    }
}
