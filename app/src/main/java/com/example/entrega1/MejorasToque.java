package com.example.entrega1;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MejorasToque extends Fragment {

    private Integer imagenes[] = {
            R.drawable.marte,
            R.drawable.marte,
            R.drawable.marte,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar,
            R.drawable.marte_mar
    };

    private String nombres[] =   {"Asteroide", "Cohete", "Tierra", "Júpiter", "Meteorito", "Satélite", "Saturno", "ISS", "Constelaciones", "Galaxia", "Sol",  "Ovni",  "Agujero negro"};
    private float cantidades[] = { 1,           3,        7,        15,        31,          63,         127,       255,   511,              1023,      2047,   4095,    8191};
    private float precios[] =    { 25,          100,      300,      1000,      2000,        5000,       10000,     25000, 75000,            200000,    600000, 1500000, 5000000};

    public MejorasToque() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_mejoras, container, false);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        ListView lista=getView().findViewById(R.id.lista);
        TextView titulo = new TextView(getActivity());
        titulo.setText(getString(R.string.mejoras_de_oxigeno_por_toque));
        titulo.setTypeface(Typeface.DEFAULT_BOLD);
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        lista.addHeaderView(titulo);
        AdaptadorMejorasToque mejoras = new AdaptadorMejorasToque(getActivity(), imagenes, nombres, cantidades, precios);
        lista.setAdapter(mejoras);
    }
}