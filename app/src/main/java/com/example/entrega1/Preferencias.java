package com.example.entrega1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class Preferencias extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferencias);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("lista_idioma")){
                    Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
                    //Locale nuevaloc = new Locale("en");
                    Locale.setDefault(nuevaloc);
                    Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
                    configuration.setLocale(nuevaloc);
                    configuration.setLayoutDirection(nuevaloc);

                    Context context = getActivity().getBaseContext().createConfigurationContext(configuration);
                    getActivity().getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
            }
        });
    }
}