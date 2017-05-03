/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analitycs.controlador.RallyTonantel;

import com.analitycs.controlador.Conexion;
import com.analitycs.modelo.DatosRally;
import com.analitycs.modelo.Rally_Meta;
import com.analitycs.modelo.Rally_Colocar;
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
@Path("Rally-Report")
public class ReporteMetas {
    /* Metodo para tomar el reporte de rallytonantel
     * Creado: 05 de Abril de 2017
     * Por: Jorge Manuel Alvarez
     */
    @GET
    @Path("generar-reportRally")
    public ArrayList<DatosRally> listRally(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFin") String fechaFin, @QueryParam("meta") int meta,@QueryParam("agencia") int agencia){
        //Array que almacena los datos
        ArrayList<DatosRally> ArrayData = new ArrayList<>();
        //Llamada a los campos de la conexion
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            //instruccion que ejecuta la accion 
            instruccion = conexion.prepareStatement(Consultasql(fechaInicio, fechaFin,meta,agencia));
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                DatosRally datosRally = new DatosRally();
                datosRally.setNombreColaborador(datos.getString("nombre"));
                datosRally.setMontoCreditos(datos.getDouble("MontoTotal"));
                datosRally.setCantidadAsociados(datos.getInt("asociados"));
                datosRally.setMetaCreditos(datos.getDouble("montoCredito"));
                datosRally.setMetaAsociados(datos.getInt("cantidadAsociados"));
                //validacion para los asociados
                if(datos.getInt("asociados")>=datos.getInt("cantidadAsociados")){
                    datosRally.setEstadoAsociados("COMPLETADOS");
                }
                else if(datos.getInt("cantidadAsociados")>datos.getInt("asociados")){
                    int sombrante = datos.getInt("cantidadAsociados")-datos.getInt("asociados");
                    datosRally.setEstadoAsociados("POR COMPLETAR: "+sombrante);
                }
                //validacion para los creeditos
                if(datos.getDouble("MontoTotal")>=datos.getDouble("montoCredito")){
                    datosRally.setEstadoCreditos("COMPLETADO");
                }
                else if(datos.getDouble("montoCredito")>datos.getDouble("MontoTotal")){
                    double sombrante = datos.getDouble("montoCredito")-datos.getDouble("MontoTotal");
                    datosRally.setEstadoCreditos("POR COMPLETAR: "+sombrante);
                }
                //carga los datos al array
                ArrayData.add(datosRally);
            }
        }
        catch(Exception e){
            System.out.println("ERROR: "+e.getMessage());
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
        
        return ArrayData;
    }
    
    
    /*
    *Metodo que contiene las consultas a ejecutarse
    * Fecha: 05 de Abril de 2015
    * Creado por: Jorge Manuel Alvarez Molina
    */
    private String Consultasql(String fechaInicio, String fechaFin, int meta, int agencia){
        //Subconsulta para obtetener la cantidad de Asociados
            String subconsultaAsocidos = "SELECT count(*) FROM rally_colocar rc " +
                "LEFT JOIN colaborador col " +
                "ON rc.colaborador_operador = col.operador " +
                "WHERE col.operador = c.operador AND tipo = 'b' ";
            
            //subconsulta para obtener el monto de creditos
            String subconsultaCreditos = "SELECT sum(monto) FROM rally_colocar rc " +
                "LEFT JOIN colaborador col " +
                "ON rc.colaborador_operador = col.operador " +
                "WHERE col.operador = c.operador AND tipo = 'a'";
            
            //consulata para obtener el reporte en general
            String sql ="SELECT c.nombre as nombre," +
                "("+subconsultaCreditos+") as MontoTotal, " +
                "("+subconsultaAsocidos+") as asociados, " +
                "m.cantidad_asociados as cantidadAsociados, "+
                "m.monto_credito as montoCredito " +
                "FROM rally_colocar r " +
                "LEFT JOIN colaborador c " +
                "ON r.colaborador_operador = c.operador " +
                "LEFT JOIN rally_meta m " +
                "ON r.rally_meta_idmeta = m.idmeta " +
                "LEFT JOIN agencia a " +
                "ON c.agencia_idagencia = a.idagencia " +
                "WHERE m.idmeta = "+meta+" " +
                "AND (r.fecha BETWEEN "+fechaInicio+" AND "+fechaFin+") AND " +
                "a.idagencia="+agencia+" "+    
                "GROUP BY c.nombre,asociados,MontoTotal ";
            
        return sql;
    }
    
    /*
    * Acciones para obtener las metas por el colaborador
    * Jorge Manuel Alvarez Molina
    * Fecha 11 de Abril de 2017
    */
    
    @GET
    @Path("consultar-metas-colaborador")
    public ArrayList<DatosRally> ReporteColaborador(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFin") String fechaFin, @QueryParam("meta") int meta,@QueryParam("agencia") int agencia){
        //Array que almacena los datos
        ArrayList<DatosRally> ArrayData = new ArrayList<>();
        //Llamada a los campos de la conexion
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
           //instruccion para realizar la consulta
           String sql = "SELECT c.nombre, c.operador "
                   + "FROM colaborador c "
                   + "LEFT JOIN agencia a "
                   + "ON c.agencia_idagencia = a.idagencia "
                   + "WHERE c.estado='a' AND a.idagencia="+agencia;
           
           //instruccion que ejecuta la accion 
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                DatosRally dr = new DatosRally();
                dr.setNombreColaborador(datos.getString("nombre"));
                dr.setRallycolocar(getdatosColaborador(datos.getInt("operador"), meta, fechaInicio, fechaFin));
                ArrayData.add(dr);
            }
        }
        catch(Exception e){
            System.out.println("ERROR: "+e.getMessage());
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
        
        return ArrayData;
    }
    
    /*
    ** Accion para obtener los datos de cada colaborador
    ** Creado por Jorge Manuel Alvarez Molina
    ** Fecha: 11 de Abril de 2017
    */
    private ArrayList<Rally_Colocar> getdatosColaborador(int operador,int idmeta,String fechaInicio,String FechaFin){
        //Llamada a los campos de la conexion
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        //ArrayList que almacena los datos momentaneamente
        ArrayList<Rally_Colocar> arrayListColocar = new ArrayList<>();
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
           //instruccion para realizar la consulta
           String sql = "SELECT * FROM rally_colocar rc " +
                "LEFT JOIN colaborador c " +
                "ON rc.colaborador_operador = c.operador " +
                "LEFT JOIN rally_meta rm " +
                "ON rc.rally_meta_idmeta =  rm.idmeta " +   
                "WHERE (rc.fecha BETWEEN '"+fechaInicio+"' AND '"+FechaFin+"') " +
                "AND c.operador="+operador+" AND rm.idmeta="+idmeta;
           
           //instruccion que ejecuta la accion 
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                Rally_Colocar rc = new Rally_Colocar();
                rc.setCif(datos.getString("cif"));
                rc.setFecha(datos.getString("fecha"));
                rc.setMonto(datos.getDouble("monto"));
                rc.setNombreAsociado(datos.getString("nombre_asociado"));
                rc.setTelefono(datos.getString("numero_telefono"));
                if("a".equals(datos.getString("tipo"))){
                    rc.setTipo("Credito");
                }
                else if("b".equals(datos.getString("tipo"))){
                    rc.setTipo("Cuenta");
                }
                rc.setReferencia(datos.getString("credito_cuenta"));
                arrayListColocar.add(rc);
            }
        }
        catch(Exception e){
            System.out.println("ERROR: "+e.getMessage());
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
        
        return arrayListColocar;
    }
    
    
    @GET
    @Path("consultar-metas")
    public  ArrayList<Rally_Meta> listagencia(){
        Rally_Meta meta = new Rally_Meta();
        return meta.Metas();
    }
}
