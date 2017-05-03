/*
 * Clase para mostrar el listado de agencias activas
 */
package com.analitycs.controlador;

import com.analitycs.modelo.Agencia;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("lista-agencias")
public class ListaAgencias {
    
    // Metodo que devuelve el listado de agencias activas
    @GET
    public ArrayList<Agencia> listaAgencia(){
        Agencia a = new Agencia();
        return a.agenciasActivas();
    }
}
