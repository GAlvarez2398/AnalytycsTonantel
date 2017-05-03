/*
 * Devuelve un reporte de referencias realizadas por cada colaborador
 */
package com.analitycs.controlador.crosselling;

import com.analitycs.controlador.Conexion;
import com.analitycs.modelo.Colaborador;
import com.analitycs.modelo.Producto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("referidos-colaborador")
public class ReferidosColaborador {
    
    // Metodo que devuelve estadistico de las referencias realizadas por cada colaborador
    @GET
    public ArrayList<Colaborador> referidos(@QueryParam("fechaFiltro") String fechaFiltro){
        Date fechaInicio = new Date(fechaFiltro);
        Date fechaFin = new Date(fechaFiltro);
        
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaFin);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        
        fechaInicio.setDate(01); // Agrega el dia inicial para la consulta
        fechaFin.setDate(calendario.getActualMaximum(Calendar.DAY_OF_MONTH)); // Agrega el maximo dia del mes para la consulta

        // Variables para conectarse con la base de datos
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        // Variable para retornar el registro de los colaboradores y sus referencias
        ArrayList<Colaborador> arrayColaborador = new ArrayList<>();
        
        // Toma el listado de los colaboradores activos
        Colaborador colaborador = new Colaborador();
        ArrayList<Colaborador> colActivos = colaborador.colaboradoresActivos();
        
        // Por cada colaborador consulta las referencias realizadas
        for(Colaborador a : colActivos){
            try{
                Class.forName(Conexion.CONTROLADOR_JDBC);
                conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
                
                String sql = "SELECT P.nombre_producto, COUNT(R.idreferencia) AS cantidad, SUM(A.monto) as monto "
                        + "FROM referencia R "
                        + "LEFT JOIN producto P "
                        + "ON R.producto_idproducto = P.idproducto "
                        + "RIGHT JOIN asignacion A "
                        + "ON R.idreferencia = A.referencia_idreferencia "
                        + "WHERE R.colaborador_operador = "+a.getOperador()+" "
                        + "AND (R.estado = 'c' AND A.estado = 'c') "
                        + "AND (A.fecha_cierre BETWEEN '"+formatoFecha.format(fechaInicio)+"' AND '"+formatoFecha.format(fechaFin)+"') "
                        + "GROUP BY P.nombre_producto";
                
                instruccion = conexion.prepareStatement(sql);
                datos = instruccion.executeQuery();
                
                // Variable para almacenar los datos del colaborador
                Colaborador datosColaborador = new Colaborador();
                datosColaborador.setAgenciaId(a.getAgenciaId());
                datosColaborador.setNombre(a.getNombre());
                
                ArrayList<Producto> arrayProducto = new ArrayList<>();
                while(datos.next()){
                    // Variable para almacenar los productos que el colaborador refirio
                    Producto p = new Producto();
                    
                    p.setNombreProducto(datos.getString("nombre_producto"));
                    p.setCantidad(datos.getInt("cantidad"));
                    p.setMonto(datos.getBigDecimal("monto"));
                    
                    arrayProducto.add(p);
                }
                
                datosColaborador.setProductosReferidos(arrayProducto);
                arrayColaborador.add(datosColaborador);
                
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                // Agregar log
            }finally{
                try{
                    if(conexion != null){
                        conexion.close();
                    }
                }
                catch(Exception e){
                    // Agregar log 
                }
            }
        }
        
        return arrayColaborador;
    }
    
}
