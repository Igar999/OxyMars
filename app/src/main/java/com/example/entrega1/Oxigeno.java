package com.example.entrega1;

public class Oxigeno {

    private static Oxigeno oxi;


    private int oxigeno;
    private int oxiToque;
    private int oxiSegundo;


    public  static Oxigeno getOxi() {
        if (oxi == null) {
            oxi = new Oxigeno();
        }
        return oxi;
    }

    private Oxigeno(){
        oxiSegundo = 0;
        oxiToque = 1;
        oxiSegundo = 0;

    }

    public void aumentarOxigenoSegundo(){
        oxigeno = oxigeno + oxiSegundo;
    }

    public void aumentarOxigenoToque(){
        oxigeno = oxigeno + oxiToque;
    }

    public Integer getOxigeno(){
        return oxigeno;
    }

    public Integer getOxiToque(){
        return oxiToque;
    }

    public Integer getOxiSegundo(){
        return oxiSegundo;
    }

    public Boolean hayOxigeno(Integer cantidad){
        return oxigeno >= cantidad;
    }

    public void quitarOxigeno(Integer cantidad){
        oxigeno = oxigeno - cantidad;
    }

    public void sumarOxiToque(Integer cantidad){
        oxiToque = oxiToque + cantidad;
    }

    public void sumarOxiSegundo(Integer cantidad){
        oxiSegundo = oxiSegundo + cantidad;
    }
}