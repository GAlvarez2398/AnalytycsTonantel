/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analitycs.controlador;

import com.analitycs.modelo.Privilegios;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author jorge-alvarez
 */
@Path("Data-User")
public class Principal {
    
    /**/
    @GET
    @Path("Privilegios")
    public ArrayList<Privilegios> GetPrivilegios(@QueryParam("data") String oper) {                
        //Creacion de array List para devolber los privilegios
        ArrayList<Privilegios> ArrayPrivilegios = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        try{
            //instaceo de la clase para desincriptar los datos
            Encriptacion e = new Encriptacion();
            String operador = e.Desencriptar(oper);
            
            
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            //Instruion sql
            String sql = "SELECT p.nombre_privilegio, p.forma_privilegio " +
                "FROM asignarprivilegio ap " +
                "LEFT JOIN privilegio p  " +
                "ON ap.privilegio_idprivilegio = p.idprivilegio " +
                "LEFT JOIN colaborador c " +
                "ON ap.colaborador_operador = c.operador " +
                "LEFT JOIN rol r " +
                "ON p.rol_idrol = r.idrol " +
                "WHERE r.idrol = 21 AND c.operador ="+operador;
            
            //Ejecuta la consulta
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                //Instanceo de la clase de Privilegios
                Privilegios p = new Privilegios();
                p.setNombre(datos.getString("nombre_privilegio"));
                p.setForma(datos.getString("forma_privilegio"));
                ArrayPrivilegios.add(p);
            }
        }   
        catch(Exception e){
            System.out.println("Error: "+e);
        }
        finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        return ArrayPrivilegios;
    }
    
    @GET
    @Path("userName")
    public String User(@QueryParam("data") String oper){
        String name = null;
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        try{
            //instaceo de la clase para desincriptar los datos
            Encriptacion e = new Encriptacion();
            String operador = e.Desencriptar(oper);
            
            //Instruion sql
            String sql = "SELECT nombre FROM colaborador WHERE operador="+operador;
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            //Ejecuta la consulta
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                name = datos.getString("nombre");
            }
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }
        finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        return  name;
    }
}
