package com.example.entrega1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

public class AdaptadorRanking extends ArrayAdapter {
    private String[] usuarios;
    private float[] cantidades;
    private Activity context;
    private Oxigeno oxi = Oxigeno.getOxi();

    public AdaptadorRanking(Activity context, String[] usuarios, float[] cantidades) {
        super(context, R.layout.fila_mejora, usuarios);
        this.context = context;
        this.usuarios = usuarios;
        this.cantidades = cantidades;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View fila=view;
        LayoutInflater inflater = context.getLayoutInflater();
        if(view==null)
            fila = inflater.inflate(R.layout.fila_ranking, null, true);
        TextView puesto = (TextView) fila.findViewById(R.id.puestoRank);
        TextView usuario = (TextView) fila.findViewById(R.id.usuarioRank);
        TextView cantidad = (TextView) fila.findViewById(R.id.cantidadRank);

        puesto.setText((position+1) + "º");
        usuario.setText(usuarios[position]);
        cantidad.setText(oxi.ponerCantidad(cantidades[position])+ " ox");

        if (position == 0){
            puesto.setTextColor(Color.parseColor("#edd013"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
        }else if (position == 1){
            puesto.setTextColor(Color.parseColor("#919191"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,31);
        }else if (position == 2){
            puesto.setTextColor(Color.parseColor("#8a381d"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,27);
        }
        return  fila;
    }
}