package com.example.entrega1;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Oxigeno {

    private static Oxigeno oxi;

    private float oxigeno;
    private float oxiToque;
    private float oxiSegundo;
    private int desbloqueadoToque;
    private int desbloqueadoSegundo;


    public void cargarOxi(float oxigeno, float oxiToque, float oxiSegundo, int desbloqueadoToque, int desbloqueadoSegundo){
        this.oxigeno = oxigeno;
        this.oxiToque = oxiToque;
        this.oxiSegundo = oxiSegundo;
        this.desbloqueadoToque = desbloqueadoToque;
        this.desbloqueadoSegundo = desbloqueadoSegundo;
    }

    public static Oxigeno getOxi() {
        if (oxi == null) {
            oxi = new Oxigeno();
        }
        return oxi;
    }

    private Oxigeno(){
        oxigeno = 0;
        oxiToque = 1;
        oxiSegundo = 0;
        desbloqueadoToque = 0;
        desbloqueadoSegundo = 0;
    }

    public void aumentarOxigenoSegundo(){
        oxigeno = oxigeno + oxiSegundo;
    }

    public void aumentarOxigenoToque(){
        oxigeno = oxigeno + oxiToque;
    }

    public float getOxigeno(){
        return oxigeno;
    }

    public float getOxiToque(){
        return oxiToque;
    }

    public float getOxiSegundo(){
        return oxiSegundo;
    }

    public Boolean hayOxigeno(float cantidad){
        return oxigeno >= cantidad;
    }

    public void quitarOxigeno(float cantidad){
        oxigeno = oxigeno - cantidad;
    }

    public void sumarOxiToque(float cantidad){
        oxiToque = oxiToque + cantidad;
    }

    public void sumarOxiSegundo(float cantidad){
        oxiSegundo = oxiSegundo + cantidad;
    }

    public Integer getDesbloqueadoSegundo(){
        return desbloqueadoSegundo;
    }

    public void desbloquearSegundo(Integer pos){
        desbloqueadoSegundo = Integer.max(desbloqueadoSegundo,pos);
    }

    public Integer getDesbloqueadoToque(){
        return desbloqueadoToque;
    }

    public void desbloquearToque(Integer pos){
        desbloqueadoToque = Integer.max(desbloqueadoToque,pos);
    }

    public String ponerCantidad(float cant){
        String cantFinal;
        String texto = "";
        if (cant >= 1000000000000.0){
            cantFinal = String.format("%.2f",(float)(cant/1000000000000.0));
            texto =  String.valueOf(cantFinal) + "B";
        }else if (cant >= 1000000000.0){
            cantFinal = String.format("%.2f",(float)(cant/1000000000.0));
            texto =  String.valueOf(cantFinal) + "KM";
        }else if (cant >= 1000000.0){
            cantFinal = String.format("%.2f",(float)(cant/1000000.0));
            texto =  String.valueOf(cantFinal) + "M";
        }else if (cant >= 1000.0){
            cantFinal = String.format("%.2f",(float)(cant/1000.0));
            texto =  String.valueOf(cantFinal) + "K";
        }else{
            cantFinal = String.valueOf(cant);
            texto = cantFinal.substring(0,cantFinal.length()-2);
        }
        return texto;
    }
}