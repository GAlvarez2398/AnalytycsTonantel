/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analitycs.modelo;

import com.analitycs.controlador.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author jorge-alvarez
 */
public class Rally_Meta {
    //declaracion de los campos de la meta
    private int idmeta;
    private String nombre;
    private char estado;
    private double monto_credito;
    private int cantida_asociados;
    
    //metodos para establecer y btener

    public int getIdmeta() {
        return idmeta;
    }

    public void setIdmeta(int idmeta) {
        this.idmeta = idmeta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public double getMonto_credito() {
        return monto_credito;
    }

    public void setMonto_credito(double monto_credito) {
        this.monto_credito = monto_credito;
    }

    public int getCantida_asociados() {
        return cantida_asociados;
    }

    public void setCantida_asociados(int cantida_asociados) {
        this.cantida_asociados = cantida_asociados;
    }
    
    // Metodo para obtener las agencias activas
    public ArrayList<Rally_Meta> Metas(){
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        ArrayList<Rally_Meta> arrayData = new ArrayList<>();
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = "SELECT * "
                    + "FROM rally_meta ";
            
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                Rally_Meta m = new Rally_Meta();
                m.setIdmeta(datos.getInt("idmeta"));
                m.setNombre(datos.getString("nombre"));
//                m.setCantida_asociados(datos.getInt("cantidad_asociados"));
                char est[] = datos.getString("estado").toCharArray();
                m.setEstado(est[0]);
                m.setMonto_credito(datos.getDouble("monto_credito"));
                
                arrayData.add(m);
            }
        }
        catch(Exception e){
            // Agregar log
            System.out.println("ERROR: "+e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                // Agregar log
                System.out.println(e.getMessage());
            }
        }
        
        return arrayData;
    }
}
