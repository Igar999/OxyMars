package com.example.entrega1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class Preferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferencias);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("algo", "aqui");
        if (key.equals("lista_idioma")){
            Locale nuevaloc = new Locale(sharedPreferences.getString("lista_idioma", "es"));
            Locale.setDefault(nuevaloc);
            Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
            configuration.setLocale(nuevaloc);
            configuration.setLayoutDirection(nuevaloc);

            Context context = getActivity().getBaseContext().createConfigurationContext(configuration);
            getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

            getActivity().finish();
            startActivity(getActivity().getIntent());
        } else if (key.equals("musica")){
            if(sharedPreferences.getBoolean("musica",true)){
                Utils.getUtils().musicaPlay();
            }
            else{
                Utils.getUtils().musicaPause();
            }
        }
    }
}