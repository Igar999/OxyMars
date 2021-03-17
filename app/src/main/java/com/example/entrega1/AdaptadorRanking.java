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
    private int[] posiciones;
    private Activity context;
    private String usu;
    private Oxigeno oxi = Oxigeno.getOxi();

    /**
     * Constructora del adaptador
     * @param context La actividad en la que se crea la lista
     * @param usuarios Lista de nombres a poner en los elementos de la lista
     * @param cantidades Lista de cantidades a poner en los elementos de la lista
     * @param posiciones Lista de posiciones a poner en los elementos de la lista
     * @param usu El usuario registrado actualmentez
     */
    public AdaptadorRanking(Activity context, String[] usuarios, float[] cantidades, int[] posiciones, String usu) {
        super(context, R.layout.fila_mejora, usuarios);
        this.context = context;
        this.usuarios = usuarios;
        this.cantidades = cantidades;
        this.posiciones = posiciones;
        this.usu = usu;
    }

    /**
     * Se crea el elemento en la posición indicada, asignandole los datos correspondientes
     * @param position La posición del elemento que se va a crear
     * @param view La vista
     * @param parent El padre
     * @return La fila de la lista
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int posicion = posiciones[position];
        View fila=view;
        LayoutInflater inflater = context.getLayoutInflater();

        fila = inflater.inflate(R.layout.fila_ranking, null, true);
        TextView puesto = (TextView) fila.findViewById(R.id.puestoRank);
        TextView usuario = (TextView) fila.findViewById(R.id.usuarioRank);
        TextView cantidad = (TextView) fila.findViewById(R.id.cantidadRank);

        puesto.setText(String.valueOf(posicion));
        usuario.setText(usuarios[posicion-1]);
        cantidad.setText(oxi.ponerCantidad(cantidades[posicion-1], true)+ " ox");
        fila.setEnabled(false);
        if (String.valueOf(posicion).equals("1")){
            puesto.setTextColor(Color.parseColor("#edd013"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
        }else if (String.valueOf(posicion).equals("2")){
            puesto.setTextColor(Color.parseColor("#919191"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,31);
        }else if (String.valueOf(posicion).equals("3")){
            puesto.setTextColor(Color.parseColor("#8a381d"));
            puesto.setTextSize(TypedValue.COMPLEX_UNIT_DIP,27);
        }

        if (usuarios[posicion-1].equals(usu)){
            usuario.setTextColor(Color.CYAN);
        }
        return  fila;
    }
}
