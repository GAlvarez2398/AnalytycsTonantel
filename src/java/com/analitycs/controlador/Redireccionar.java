/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.analitycs.controlador;

import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author jorge-alvarez
 */
@Path("Encriptar")
public class Redireccionar {
    // Metodo que devuelve el listado de agencias activas
    @GET
    public String Redireccionar(@QueryParam("operConect") String operador){
        //instanceo de la clase de encriptacion
        Encriptacion e = new Encriptacion();
        return  e.Encriptar(operador);
    }
}
