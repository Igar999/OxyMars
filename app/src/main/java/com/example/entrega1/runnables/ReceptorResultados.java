package com.example.entrega1.runnables;

import android.content.Context;

import org.json.JSONArray;


public class ReceptorResultados {

    private static ReceptorResultados receptorResultados;
    private Context context;

    private String resUsuario;
    private String resExiste;
    private JSONArray resRanking;

    private Boolean finUsuario = false;
    private Boolean finExiste = false;
    private Boolean finRanking = false;
    private Boolean finCrear = false;
    private Boolean finCargar = false;




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

    public Boolean haAcabadoCrear(){
        return finCrear;
    }

    public void setFinCrear(Boolean finCrear) {
        this.finCrear = finCrear;
    }

    public Boolean haAcabadoCargar(){
        return finCargar;
    }

    public void setFinCargar(Boolean finCargar) {
        this.finCargar = finCargar;
    }
}
