/*
 * Clase POJO para la entidad Colaborador
 */
package com.analitycs.modelo;

import com.analitycs.controlador.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Colaborador {
    // Declaracion de campos de la clase
    private int operador;
    private String usuario;
    private String clave;
    private char estado;
    private String nombre;
    private int agenciaId;
    private ArrayList<Producto> productosReferidos;
    
    // Declaracion de metodos establecer y obtener
    public int getOperador() {
        return operador;
    }

    public void setOperador(int operador) {
        this.operador = operador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAgenciaId() {
        return agenciaId;
    }

    public void setAgenciaId(int agenciaId) {
        this.agenciaId = agenciaId;
    }

    public ArrayList<Producto> getProductosReferidos() {
        return productosReferidos;
    }

    public void setProductosReferidos(ArrayList<Producto> productosReferidos) {
        this.productosReferidos = productosReferidos;
    }
    
    // Metodo para obtener el listado de colaboradores activos
    public ArrayList<Colaborador> colaboradoresActivos(){
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        ArrayList<Colaborador> arrayColaborador = new ArrayList<>();
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = "SELECT * "
                    + "FROM colaborador "
                    + "WHERE estado = 'a' "
                    + "ORDER BY nombre";
            
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                Colaborador v = new Colaborador();
                v.setOperador(datos.getInt("operador"));
                v.setNombre(datos.getString("nombre"));
                v.setAgenciaId(datos.getInt("agencia_idagencia"));
                
                arrayColaborador.add(v);
            }
        }
        catch(Exception e){
            // Agregar log para de exception
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                // Agregar exception de error de conexion
            }
        }
        return arrayColaborador;
    }
    
}
