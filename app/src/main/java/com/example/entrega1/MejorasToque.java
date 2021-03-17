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

    public MejorasToque() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_mejoras, container, false);
    }

    /**
     * Se crean las listas de datos manualmente, se crea la cabecera de la lista y se establece el adaptador
     * @param saveInstanceState
     */
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        Integer[] imagenes = {
                R.drawable.asteroide,
                R.drawable.cohete,
                R.drawable.tierra,
                R.drawable.jupiter,
                R.drawable.meteorito,
                R.drawable.satelite,
                R.drawable.saturno,
                R.drawable.iss,
                R.drawable.constelacion,
                R.drawable.galaxia,
                R.drawable.sol,
                R.drawable.ovni,
                R.drawable.agujeronegro
        };

        String[] nombres =   {getString(R.string.asteroide), getString(R.string.cohete), getString(R.string.tierra), getString(R.string.jupiter), getString(R.string.meteorito), getString(R.string.satelite), getString(R.string.saturno), getString(R.string.iss), getString(R.string.constelaciones), getString(R.string.galaxia), getString(R.string.sol), getString(R.string.ovni), getString(R.string.agujeronegro)};
        float[] cantidades = { 1,           3,        7,        15,        31,          63,         127,       255,   511,              1023,      2047,   4095,    8191};
        float[] precios =    { 25,          100,      300,      1000,      2000,        5000,       10000,     25000, 75000,            200000,    600000, 1500000, 5000000};
        //float[] precios =    { 0,    0,    0,      0,    0,    0,      0,      0,    0,   0,    0,    0, 0};

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