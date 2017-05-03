/*
 * Clase para administrar la entidad agencia
 */
package com.analitycs.modelo;

import com.analitycs.controlador.Conexion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Agencia {
    // Declaracion de campos de la clase
    private int idAgencia;
    private String nombreAgencia;
    private char estado;
    private Date fechaModificacion;
    private int idEmpresa;
    
    // Declaracion de campos establecer y obtener
    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int idAgencia) {
        this.idAgencia = idAgencia;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    // Metodo para obtener las agencias activas
    public ArrayList<Agencia> agenciasActivas(){
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        ArrayList<Agencia> arrayAgencia = new ArrayList<>();
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = "SELECT * "
                    + "FROM agencia "
                    + "WHERE estado = 'a'";
            
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                Agencia a = new Agencia();
                a.setIdAgencia(datos.getInt("idagencia"));
                a.setNombreAgencia(datos.getString("nombre_agencia"));
                char est[] = datos.getString("estado").toCharArray();
                a.setEstado(est[0]);
                a.setFechaModificacion(datos.getDate("fecha_modificacion"));
                a.setIdEmpresa(datos.getInt("empresa_idempresa"));
                
                arrayAgencia.add(a);
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
        
        return arrayAgencia;
    }
}
