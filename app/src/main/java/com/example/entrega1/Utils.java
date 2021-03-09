package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


public class Utils {

    private static Utils utils;

    public static Utils getUtils() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    private Utils(){
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

}
