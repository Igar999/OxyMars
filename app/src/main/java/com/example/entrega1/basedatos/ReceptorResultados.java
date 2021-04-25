package com.example.entrega1.basedatos;

import android.content.Context;

import org.json.JSONArray;


public class ReceptorResultados {

    private static ReceptorResultados receptorResultados;
    private Context context;

    private String resUsuario;
    private String resExiste;
    private JSONArray resRanking;
    private String resCrear;

    private Boolean finUsuario = false;
    private Boolean finExiste = false;
    private Boolean finRanking = false;
    private Boolean finCrear = false;
    private Boolean finCargar = false;
    private Boolean finActualizar = false;


    /**
     * Crea una instancia de la clase o devuelve la que ya existe
     * @return Instancia de la clase Utils (Singleton)
     */
    public static ReceptorResultados getReceptorResultados() {
        if (receptorResultados == null) {
            receptorResultados = new ReceptorResultados();
        }
        return receptorResultados;
    }

    /**
     * Constructora de la clase
     */
    private ReceptorResultados(){
    }


    //Métodos para hacer get y set de los parámetros relativos a la comprobación de usuario y contraseña para saber si son correctos
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    //res almacena el resultado obtenido en el Runnable
    public Boolean haAcabadoUsuario(){
        return finUsuario;
    }

    public String obtenerResultadoUsuario(){
        finUsuario = false;
        return resUsuario;
    }

    public void setFinUsuario(Boolean finUsuario) {
        this.finUsuario = finUsuario;
    }

    public void setResUsuario(String resUsuario) {
        this.resUsuario = resUsuario;
    }




    //Métodos para hacer get y set de los parámetros relativos a la comprobación de usuario para saber si existe
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    //res almacena el resultado obtenido en el Runnable
    public Boolean haAcabadoExiste(){
        return finExiste;
    }

    public String obtenerResultadoExiste(){
        finExiste = false;
        return resExiste;
    }

    public void setFinExiste(Boolean finExiste) {
        this.finExiste = finExiste;
    }

    public void setResExiste(String resExiste) {
        this.resExiste = resExiste;
    }




    //Métodos para hacer get y set de los parámetros relativos a la obtención de datos para el ranking
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    //res almacena el resultado obtenido en el Runnable
    public Boolean haAcabadoRanking(){
        return finRanking;
    }

    public JSONArray obtenerResultadoRanking(){
        finRanking = false;
        return resRanking;
    }

    public void setFinRanking(Boolean finRanking) {
        this.finRanking = finRanking;
    }

    public void setResRanking(JSONArray resRanking) {
        this.resRanking = resRanking;
    }




    //Métodos para hacer get y set de los parámetros relativos a la creación de usuario
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    //res almacena el resultado obtenido en el Runnable
    public Boolean haAcabadoCrear(){
        return finCrear;
    }

    public void setFinCrear(Boolean finCrear) {
        this.finCrear = finCrear;
    }

    public void setResCrear(String resCrear) {
        this.resCrear = resCrear;
    }

    public String obtenerResultadoCrear(){
        return resCrear;
    }




    //Métodos para hacer get y set de los parámetros relativos a la obtención de datos de usuario
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    public Boolean haAcabadoCargar(){
        return finCargar;
    }

    public void setFinCargar(Boolean finCargar) {
        this.finCargar = finCargar;
    }




    //Métodos para hacer get y set de los parámetros relativos a la actualización de datos de usuario
    //fin almacena si la ejecución del Runnable ha llegado a su fin
    public Boolean haAcabadoActualizar(){
        Boolean ret;
        if (finActualizar){
            ret = true;
            finActualizar = false;
        }
        else {
            ret = false;
        }
        return ret;
    }

    public void setFinActualizar(Boolean finActualizar) {
        this.finActualizar = finActualizar;
    }
}
