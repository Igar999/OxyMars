package com.example.entrega1;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class MejorasSegundo extends Fragment {

    public MejorasSegundo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_mejoras, container, false);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        Integer[] imagenes = {
                R.drawable.mar,
                R.drawable.rio,
                R.drawable.hierba,
                R.drawable.arena,
                R.drawable.hielo,
                R.drawable.arboles,
                R.drawable.montanas,
                R.drawable.flores,
                R.drawable.nieve,
                R.drawable.humanos,
                R.drawable.pueblos,
                R.drawable.minas,
                R.drawable.ciudades
        };

        String[] nombres =   {getString(R.string.mar), getString(R.string.rios), getString(R.string.hierba), getString(R.string.arena), getString(R.string.hielo), getString(R.string.arboles), getString(R.string.montanas), getString(R.string.flores), getString(R.string.nieve), getString(R.string.humanos), getString(R.string.pueblos), getString(R.string.minas), getString(R.string.ciudades)};
        float[] cantidades = { 1,     5,      10,       30,      50,      75,        150,        250,      600,     1500,      3000,      7500,    20000};
        //float[] precios =    { 30,    150,    450,      1500,    2000,    6000,      10000,      22500,    80000,   300000,    800000,    2500000, 10000000};
        float[] precios =    { 0,    0,    0,      0,    0,    0,      0,      0,    0,   0,    0,    0, 0};

        ListView lista=getView().findViewById(R.id.lista);
        TextView titulo = new TextView(getActivity());
        titulo.setText(getString(R.string.mejoras_de_oxigeno_por_segundo));
        titulo.setTypeface(Typeface.DEFAULT_BOLD);
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        lista.addHeaderView(titulo);
        AdaptadorMejorasSegundo mejoras = new AdaptadorMejorasSegundo(getActivity(), imagenes, nombres, cantidades, precios);
        lista.setAdapter(mejoras);
    }
}