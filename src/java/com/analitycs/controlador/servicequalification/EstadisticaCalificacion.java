/*
 * Clase que proporciona los servicios para mostrar estadisticas de agencia
 * con datos del modulo Service Qualification
 */
package com.analitycs.controlador.servicequalification;

import com.analitycs.controlador.Conexion;
import com.analitycs.modelo.TotalCalificacion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("estadistica-calificacion")
public class EstadisticaCalificacion {
    
    @GET
    @Path("calificacion-total")
    public ArrayList<TotalCalificacion> calificacionTotal(@QueryParam("idAgencia") String idAgencia){
        System.out.println(idAgencia);
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement instruccion;
        ResultSet datos = null;
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = null;
            if(idAgencia.equals("0")){
                sql = "SELECT parametrocalificacion_codigo_parametro AS parametro, "
                    + "COUNT(idcalifica) AS total "
                    + "FROM califica "
                    + "GROUP BY parametrocalificacion_codigo_parametro";
            }
            else{
                sql = "SELECT parametrocalificacion_codigo_parametro AS parametro, "
                    + "COUNT(idcalifica) AS total "
                    + "FROM califica "
                    + "WHERE agencia_idagencia = "+idAgencia+" "
                    + "GROUP BY parametrocalificacion_codigo_parametro";
            }

            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            TotalCalificacion tc = new TotalCalificacion();
            while(datos.next()){
                switch(datos.getString("parametro")){
                    case "e":
                        tc.setExcelente(datos.getInt("total"));
                        break;
                        
                    case "b":
                        tc.setBueno(datos.getInt("total"));
                        break;
                        
                    case "r":
                        tc.setRegular(datos.getInt("total"));
                        break;
                        
                    case "m":
                        tc.setMalo(datos.getInt("total"));
                        break;
                }
            }
            arrayCalificacion.add(tc);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        return arrayCalificacion;
    }
    
    @GET
    @Path("calificacion-caja")
    public ArrayList<TotalCalificacion> calificacionCaja(@QueryParam("idAgencia") String idAgencia){
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        arrayCalificacion = calificacionDepartamento("Caja", idAgencia);
        return arrayCalificacion;
    }
    
    @GET
    @Path("calificacion-secretaria")
    public ArrayList<TotalCalificacion> calificacionSecretaria(@QueryParam("idAgencia") String idAgencia){
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        arrayCalificacion = calificacionDepartamento("Cuentas Nuevas", idAgencia);
        return arrayCalificacion;
    }
    
    @GET
    @Path("calificacion-creditos")
    public ArrayList<TotalCalificacion> calificacionCreditos(@QueryParam("idAgencia") String idAgencia){
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        arrayCalificacion = calificacionDepartamento("Creditos y Cobros", idAgencia);
        return arrayCalificacion;
    }
    
    @GET
    @Path("calificacion-seguros")
    public ArrayList<TotalCalificacion> calificacionSeguros(@QueryParam("idAgencia") String idAgencia){
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        arrayCalificacion = calificacionDepartamento("Seguros", idAgencia);
        return arrayCalificacion;
    }
    
    // Metodo para tomar la calificacion por departamento
    private ArrayList<TotalCalificacion> calificacionDepartamento(String departamento, String idAgencia){
        ArrayList<TotalCalificacion> arrayCalificacion = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = null;
            if(idAgencia.equals("0")){
                sql = "SELECT parametrocalificacion_codigo_parametro AS parametro, "
                    + "COUNT(idcalifica) total "
                    + "FROM califica "
                    + "WHERE departamento_iddepartamento = ( "
                    + "SELECT iddepartamento "
                    + "FROM departamento "
                    + "WHERE nombre_departamento LIKE '%"+departamento+"%' "
                    + ") GROUP BY parametrocalificacion_codigo_parametro";
            }
            else{
                sql = "SELECT parametrocalificacion_codigo_parametro AS parametro, "
                    + "COUNT(idcalifica) total "
                    + "FROM califica "
                    + "WHERE agencia_idagencia = "+idAgencia+" "
                    + "AND departamento_iddepartamento = ( "
                    + "SELECT iddepartamento "
                    + "FROM departamento "
                    + "WHERE nombre_departamento LIKE '%"+departamento+"%' "
                    + ") GROUP BY parametrocalificacion_codigo_parametro";
            }
            
                
            
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            TotalCalificacion t = new TotalCalificacion();
            while(datos.next()){
                switch(datos.getString("parametro")){
                    case "e":
                        t.setExcelente(datos.getInt("total"));
                        break;
                        
                    case "b":
                        t.setBueno(datos.getInt("total"));
                        break;
                        
                    case "r":
                        t.setRegular(datos.getInt("total"));
                        break;
                        
                    case "m":
                        t.setMalo(datos.getInt("total"));
                        break;
                }
            }
            
            // Agrega el objeto al array para mostrarlos en un JSON
            arrayCalificacion.add(t);
        }
        catch(Exception e){
            System.out.println(e);
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        
        return arrayCalificacion;
    }
    
}
