package com.example.entrega1;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class Utils {

    private static Utils utils;
    private MediaPlayer musica;
    private Context context;
    private Boolean musicaLista = false;
    private String usuario;

    /**
     * Crea una instancia de la clase o devuelve la que ya existe
     * @return Instancia de la clase Utils (Singleton)
     */
    public static Utils getUtils() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    /**
     * Constructora de la clase
     */
    private Utils(){
    }

    /**
     * Almacena el contexto, para poder utilizarlo en métodos que requieren de contexto, pero que se usan en varias clases distintas
     * @param context
     */
    public void setContext(Context context){
        this.context = context;
    }


    /**
     * Pasa un fragment de visible a invisible, y viceversa
     * @param fragment El fragment cuya visibilidad se quiere cambiar
     * @param actividad La actividad en la que está el fragment
     */
    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void showHideFragment(final Fragment fragment, AppCompatActivity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (fragment.isHidden()) {
            ft.show(fragment);
        } else {
            ft.hide(fragment);
        }
        ft.commit();
    }

    /**
     * Esconde un fragment que está visible
     * @param fragment El fragment a esconder
     * @param actividad La actividad en la que está el fragment
     */
    public void showFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (fragment.isHidden()) {
            ft.show(fragment);
        }
        ft.commit();
    }

    /**
     * Muestra un fragment que está invisible
     * @param fragment El fragment a mostrar
     * @param actividad La actividad en la que está el fragment
     */
    public void hideFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (!fragment.isHidden()) {
            ft.hide(fragment);
        }
        ft.commit();
    }

    /**
     * Reproduce la musica almacenada en la variable si no está sonando y el usuario ha puesto en las preferencias que quiere música de fondo
     */
    public void musicaPlay(){
        if (!musica.isPlaying() && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("musica", true)){
            musica.start();
        }
    }

    /**
     * Pausa la música almacenada en la variable si está sonando
     */
    public void musicaPause(){
        if (musica.isPlaying()){
            musica.pause();
        }
    }

    /**
     * Almacena el MediaPlayer en la variable musica para poder acceder a ella en el futuro, y la empieza
     * @param context
     */
    public void empezarMusica(Context context){
        if (musica == null){
            musica = MediaPlayer.create(context, R.raw.musicafondo);
            musica.setLooping(true); // Set looping
            musicaPlay();
            musicaLista = true;
        }
    }

    /**
     * Devuelve true si la musica está preparada, y false si tiene que crearse aún el MediaPlayer
     * @return Booleano que indica si está lista la música
     */
    public Boolean musicaLista(){
        return this.musicaLista;
    }

    /**
     * Reproduce un sonido si los sonidos están activados en las preferencias
     * @param context El contexto de la actividad en la que se va a reproducir el sonido
     * @param sonido El sonido a reproducir
     */
    public void reproducirSonido(Context context, int sonido){
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sonido",true)){
            MediaPlayer mediaPlayer = MediaPlayer.create(context,sonido);
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(1,1);
                    mp.start();
                }

            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }

    /**
     * Devuelve el usuario, que se almacena en una variable local
     * @return El nombre de usuario
     */
    public String getUsuario(){
        return this.usuario;
    }

    /**
     * Almacena el nombre de usuario, para poder acceder a él en el futuro
     * @param nombre El nombre a establecer como nombre de usuario
     */
    public void setUsuario(String nombre){
        this.usuario = nombre;
    }


}
