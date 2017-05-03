/*Clase para generar el reporte de comisiones del modulo crosselling
Creado: 22 de febrero del 2017
Por: Rigo Galica
*/

package com.analitycs.controlador.crosselling;

import com.analitycs.controlador.Conexion;
import com.analitycs.modelo.DatosComision;
import com.analitycs.modelo.DetalleComision;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("comisiones")
public class Comisiones {
    
    /* Metodo para tomar el listado de colaboradores que comisionan en la fecha indicada
     * Creado: 22 de febrero del 2018
     * Por: Rigo Galicia
     */
    private ArrayList<DatosComision> colaboradoresComision(String fechaInicio, String fechaFin){
        ArrayList<DatosComision> arrayColaboradores = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        
        try{
            Class.forName(Conexion.CONTROLADOR_JDBC);
            conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);
            
            String sql = "SELECT DISTINCT C.agencia_idagencia AS idAgencia, "
                    + "C.operador, "
                    + "C.nombre "
                    + "FROM colaborador C "
                    + "RIGHT JOIN referencia R "
                    + "ON C.operador = R.colaborador_operador "
                    + "RIGHT JOIN asignacion N "
                    + "ON R.idreferencia = N.referencia_idreferencia "
                    + "WHERE (R.estado = 'c' AND N.estado = 'c') "
                    + "AND N.fecha_cierre BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' "
                    + "ORDER BY C.nombre";
            
            instruccion = conexion.prepareStatement(sql);
            datos = instruccion.executeQuery();
            
            while(datos.next()){
                DatosComision dc = new DatosComision();
                dc.setIdAgencia(datos.getInt("idAgencia"));
                dc.setOperador(datos.getInt("operador"));
                dc.setNombre(datos.getString("nombre"));
                
                arrayColaboradores.add(dc);
            }
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
        
        return arrayColaboradores;
    }
    
    /* Metodo utilizado para generar las comisiones por colaborador
     * Creado: 22 de febrero del 2017
     * Por: Rigo Galicia
     */
    @GET
    @Path("generar-comisiones")
    public ArrayList<DatosComision> generarComisiones(@QueryParam("fechaInicio") String fechaInicio, @QueryParam("fechaFin") String fechaFin){
        ArrayList<DatosComision> arrayComisiones = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement instruccion = null;
        ResultSet datos = null;
        
        ArrayList<DatosComision> listaColaboradores = colaboradoresComision(fechaInicio, fechaFin);
        for(DatosComision dc : listaColaboradores){
             try{
                Class.forName(Conexion.CONTROLADOR_JDBC);
                conexion = DriverManager.getConnection(Conexion.URL_BASEDATOS, Conexion.USUARIO, Conexion.CLAVE);

                String sql = "SELECT P.nombre_producto AS producto, "
                        + "COUNT(idreferencia) AS cantidad, "
                        + "C.vendedor AS comision, "
                        + "(COUNT(idreferencia) * C.vendedor) AS total "
                        + "FROM referencia R "
                        + "LEFT JOIN asignacion N "
                        + "ON R.idreferencia = N.referencia_idreferencia "
                        + "LEFT JOIN producto P "
                        + "ON R.producto_idproducto = P.idproducto "
                        + "RIGHT JOIN preciocomision C "
                        + "ON P.idproducto = C.producto_idproducto "
                        + "WHERE R.colaborador_operador = "+dc.getOperador()+" "
                        + "AND(R.estado = 'c' AND N.estado = 'c') "
                        + "AND N.fecha_cierre BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' "
                        + "GROUP BY P.nombre_producto";

                instruccion = conexion.prepareStatement(sql);
                datos = instruccion.executeQuery();

                // Agrega los datos a un nuevo objeto de datos comision
                DatosComision datosComision = new DatosComision();
                datosComision.setIdAgencia(dc.getIdAgencia());
                datosComision.setOperador(dc.getOperador());
                datosComision.setNombre(dc.getNombre());
                
                ArrayList<DetalleComision> arrayDetalle = new ArrayList<>();
                while(datos.next()){
                    DetalleComision detalleComision = new DetalleComision();
                    detalleComision.setProducto(datos.getString("producto"));
                    detalleComision.setCantidad(datos.getInt("cantidad"));
                    detalleComision.setComision(datos.getDouble("comision"));
                    detalleComision.setTotal(datos.getDouble("total"));
                    
                    arrayDetalle.add(detalleComision);
                }
                datosComision.setDetalleComision(arrayDetalle);
                arrayComisiones.add(datosComision);
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
        }

        return arrayComisiones;
    }
}
