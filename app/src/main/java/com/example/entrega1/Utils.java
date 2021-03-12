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


    public static Utils getUtils() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    private Utils(){
    }

    public void setContext(Context context){
        this.context = context;
    }

    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void showHideFragment(final Fragment fragment, AppCompatActivity contexto){
        FragmentTransaction ft = contexto.getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (fragment.isHidden()) {
            ft.show(fragment);
        } else {
            ft.hide(fragment);
        }
        ft.commit();
    }

    public void showFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (fragment.isHidden()) {
            ft.show(fragment);
        }
        ft.commit();
    }
    public void hideFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (!fragment.isHidden()) {
            ft.hide(fragment);
        }
        ft.commit();
    }

    public void musicaPlay(){
        if (!musica.isPlaying() && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("musica", true)){
            musica.start();
        }
    }

    public void musicaPause(){
        if (musica.isPlaying()){
            musica.pause();
        }
    }

    public void musicaStop(){
        musica.release();
    }

    public void subirVolumen(){
        float vol=(float)(Math.log(100-50)/Math.log(100));
        musica.setVolume(vol,vol);
    }

    public void bajarVolumen(){
        float vol=(float)(Math.log(100-99)/Math.log(100));
        musica.setVolume(vol,vol);
    }

    public void empezarMusica(Context context){
        if (musica == null){
            musica = MediaPlayer.create(context, R.raw.musicafondo);
            musica.setLooping(true); // Set looping
            //musica.setVolume(100, 100);
            musicaPlay();
            musicaLista = true;
        }
    }

    public Boolean musicaLista(){
        return this.musicaLista;
    }

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


}
