package com.example.entrega1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;

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

    private Context context;

    private float oxigeno;
    private float oxiToque;
    private float oxiSegundo;
    private int desbloqueadoToque;
    private int desbloqueadoSegundo;


    public void cargarOxi(Context context, float oxigeno, float oxiToque, float oxiSegundo, int desbloqueadoToque, int desbloqueadoSegundo){
        this.context = context;
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
        desbloqueadoToque = -1;
        desbloqueadoSegundo = -1;
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

    public String ponerCantidad(float cant, Boolean forzar){
        String texto = String.valueOf(cant);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (prefs.getBoolean("notacion", true) || forzar){
            String cantFinal;
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
        }else{
            texto = texto.substring(0,texto.length()-2);
        }
        return texto;
    }

    public void actualizarInterfaz(Activity activity) {
        //DESBLOQUEADO VA DE 0 a 12
        // -1 = nada
        // 0 =
        int desToque = desbloqueadoToque;

        ImageView asteroide = activity.findViewById(R.id.imgAsteroide);
        ImageView cohete = activity.findViewById(R.id.imgCohete);
        ImageView tierra = activity.findViewById(R.id.imgTierra);
        ImageView jupiter = activity.findViewById(R.id.imgJupiter);
        ImageView meteorito = activity.findViewById(R.id.imgMeteorito);
        ImageView satelite = activity.findViewById(R.id.imgSatelite);
        ImageView saturno = activity.findViewById(R.id.imgSaturno);
        ImageView iss = activity.findViewById(R.id.imgISS);
        ImageView constelaciones = activity.findViewById(R.id.imgConstelaciones);
        ImageView galaxia = activity.findViewById(R.id.imgGalaxia);
        ImageView sol = activity.findViewById(R.id.imgSol);
        ImageView ovni = activity.findViewById(R.id.imgOvni);
        ImageView agujeroNegro = activity.findViewById(R.id.imgAgujeroNegro);

        ImageView[] lista = new ImageView[]{asteroide,cohete,tierra,jupiter,meteorito,satelite,saturno,iss,constelaciones,galaxia,sol,ovni,agujeroNegro};

        for(ImageView item:lista){
            item.setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<lista.length;i++){
            if(desToque-1 >= i){
                lista[i].setVisibility(View.VISIBLE);
            }
        }

        int desSegundo = desbloqueadoSegundo;
        ImageView marte = activity.findViewById(R.id.planeta);

        String nombre = "marte"+desSegundo;

        int id = activity.getResources().getIdentifier(nombre, "drawable", activity.getPackageName());
        activity.findViewById(R.id.planeta).setBackground(activity.getDrawable(id));



    }

}