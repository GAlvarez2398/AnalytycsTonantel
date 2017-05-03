/*
 * Clase que contiene los parametros para la conexion con la base de datos
 */
package com.analitycs.controlador;

public class Conexion {
    // Declaracion de campos
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "applicationmanager";
    public static final String USUARIO = "root";
    public static final String CLAVE = "root";
    public static final String CONTROLADOR_JDBC = "com.mysql.jdbc.Driver";
    public static final String URL_BASEDATOS = "jdbc:mysql://"+IP+":"+PUERTO+"/"+BASE_DATOS+"";
}
