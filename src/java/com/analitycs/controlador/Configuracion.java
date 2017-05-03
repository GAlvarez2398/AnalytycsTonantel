/*
 * Clase de configuracion del Web Service
 */
package com.analitycs.controlador;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("web-service")
public class Configuracion extends Application{
    
     @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }
    
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.analitycs.controlador.ListaAgencias.class);
        resources.add(com.analitycs.controlador.Principal.class);
        resources.add(com.analitycs.controlador.RallyTonantel.ReporteMetas.class);
        resources.add(com.analitycs.controlador.Redireccionar.class);
        resources.add(com.analitycs.controlador.crosselling.Comisiones.class);
        resources.add(com.analitycs.controlador.crosselling.ReferidosColaborador.class);
        resources.add(com.analitycs.controlador.servicequalification.EstadisticaCalificacion.class);
    }
    
}
