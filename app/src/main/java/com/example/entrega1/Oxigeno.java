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


    /**
     * Se almecenan en la clase los parámetros que se utilizarán en el futuro
     * @param context El contexto
     * @param oxigeno El oxígeno
     * @param oxiToque El oxígeno por toque
     * @param oxiSegundo El oxígeno por segundo
     * @param desbloqueadoToque La cantidad de mejoras de oxígeno por toque desbloqueadas
     * @param desbloqueadoSegundo La cantidad de mejoras de oxígeno por segundo desbloqueadas
     */
    /*public void cargarOxi(Context context, float oxigeno, float oxiToque, float oxiSegundo, int desbloqueadoToque, int desbloqueadoSegundo){
        this.context = context;
        this.oxigeno = oxigeno;
        this.oxiToque = oxiToque;
        this.oxiSegundo = oxiSegundo;
        this.desbloqueadoToque = desbloqueadoToque;
        this.desbloqueadoSegundo = desbloqueadoSegundo;
    }*/

    /**
     * Se actualizan en la clase los valores de los parámetros
     * @param oxigeno El oxígeno
     * @param oxiToque El oxígeno por toque
     * @param oxiSegundo El oxígeno por segundo
     * @param desbloqueadoToque La cantidad de mejoras de oxígeno por toque desbloqueadas
     * @param desbloqueadoSegundo La cantidad de mejoras de oxígeno por segundo desbloqueadas
     */
    public void actualizarValores(float oxigeno, float oxiToque, float oxiSegundo, int desbloqueadoToque, int desbloqueadoSegundo){
        this.oxigeno = oxigeno;
        this.oxiToque = oxiToque;
        this.oxiSegundo = oxiSegundo;
        this.desbloqueadoToque = desbloqueadoToque;
        this.desbloqueadoSegundo = desbloqueadoSegundo;
    }

    public void setContext(Context pContext) {
        this.context = pContext;
    }


    /**
     * Crea una instancia de la clase o devuelve la que ya existe
     * @return instancia de la clase Oxigeno (Singleton)
     */
    public static Oxigeno getOxi() {
        if (oxi == null) {
            oxi = new Oxigeno();
        }
        return oxi;
    }

    /**
     * Constructora de la clase, establece unos valores por defecto
     */
    private Oxigeno(){
        oxigeno = 0;
        oxiToque = 1;
        oxiSegundo = 0;
        desbloqueadoToque = -1;
        desbloqueadoSegundo = -1;
    }

    /**
     * Suma la cantidad correspondiente de oxígeno por segundo al oxígeno total
     */
    public void aumentarOxigenoSegundo(){
        oxigeno = oxigeno + oxiSegundo;
    }

    /**
     * Suma la cantidad correspondiente de oxígeno por segundo al oxígeno total
     */
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

    /**
     * Comprueba si hay suficiente oxígeno para comprar una mejora
     * @param cantidad El coste de la mejora
     * @return Booleano que representa si hay más que esa cantidad
     */
    public Boolean hayOxigeno(float cantidad){
        return oxigeno >= cantidad;
    }

    /**
     * Resta una cantidad del oxígeno total
     * @param cantidad La cantidad a restar
     */
    public void quitarOxigeno(float cantidad){
        oxigeno = oxigeno - cantidad;
    }

    /**
     * Aumenta el oxígeno por toque en una cantidad
     * @param cantidad La cantidad a aumentar
     */
    public void sumarOxiToque(float cantidad){
        oxiToque = oxiToque + cantidad;
    }

    /**
     * Aumenta el oxígeno por segundo en una cantidad
     * @param cantidad La cantidad a aumentar
     */
    public void sumarOxiSegundo(float cantidad){
        oxiSegundo = oxiSegundo + cantidad;
    }

    public Integer getDesbloqueadoSegundo(){
        return desbloqueadoSegundo;
    }

    /**
     * Establece el número de mejoras desbloqueadas como el máximo entre la mejora más alta desbloqueada, que está almacenada en la propia clase, y la que se le pasa como parámetro, que es la que se acaba de comprar
     * @param pos La posición de la mejora que se acaba de comprar
     */
    public void desbloquearSegundo(Integer pos){
        desbloqueadoSegundo = Integer.max(desbloqueadoSegundo,pos);
    }

    public Integer getDesbloqueadoToque(){
        return desbloqueadoToque;
    }
    /**
     * Establece el número de mejoras desbloqueadas como el máximo entre la mejora más alta desbloqueada, que está almacenada en la propia clase, y la que se le pasa como parámetro, que es la que se acaba de comprar
     * @param pos La posición de la mejora que se acaba de comprar
     */

    public void desbloquearToque(Integer pos){
        desbloqueadoToque = Integer.max(desbloqueadoToque,pos);
    }

    /**
     * Transforma una cantidad numérica a String, y si el usuario lo desea, lo transforma a notación simplificada
     * @param cant La cantidad a transformar
     * @param forzar Si se desea pasar a notación simplificada aunque en las preferencias diga que no
     * @return La cantidad en formato string con la notación correspondiente
     */
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

    /**
     * En caso de las mejoras por toque, oculta y muestra las imágenes dependiendo del número de mejoras desbloqueadas
     * En caso de las mejoras por segundo, cambia la imágen del planeta a la que representa las mejoras desbloqueadas
     * @param activity La actividad en la que se quiere actualizar la interfaz
     */
    public void actualizarInterfaz(Activity activity) {
        int desToque = desbloqueadoToque;
        if (desToque == -1){
            desToque = 0;
        }
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
        if (desSegundo == -1){
            desSegundo = 0;
        }
        String nombre = "marte"+desSegundo;
        int id = activity.getResources().getIdentifier(nombre, "drawable", activity.getPackageName());
        activity.findViewById(R.id.planeta).setBackground(activity.getDrawable(id));
    }

}