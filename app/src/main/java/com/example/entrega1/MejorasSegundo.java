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

    private Integer imagenes[] = {
            R.drawable.marte_mar,
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
            R.drawable.marte_mar
    };

    private String nombres[] = {"Cohete", "Nave", "Ovni", "ISS", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo", "Algo"};

    private Integer cantidades[] = {1, 5, 20, 80, 100, 100, 100, 100, 100, 100, 100, 100, 100};

    private Integer precios[] = {10, 50, 200, 800, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};

    public MejorasSegundo() {
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
        titulo.setText("MEJORAS DE OXIGENO POR SEGUNDO");
        titulo.setTypeface(Typeface.DEFAULT_BOLD);
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
        titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        lista.addHeaderView(titulo);
        AdaptadorMejorasSegundo mejoras = new AdaptadorMejorasSegundo(getActivity(), imagenes, nombres, cantidades, precios);
        lista.setAdapter(mejoras);
    }
}