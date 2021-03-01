package com.example.entrega1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorMejorasSegundo extends ArrayAdapter {
    private Integer[] imagenes;
    private String[] nombres;
    private Integer[] cantidades;
    private Integer[] precios;
    private Activity context;
    private Oxigeno oxi = Oxigeno.getOxi();

    public AdaptadorMejorasSegundo(Activity context, Integer[] imagenes, String[] nombres, Integer[] cantidades, Integer[] precios) {
        super(context, R.layout.fila_mejora, nombres);
        this.context = context;
        this.imagenes = imagenes;
        this.nombres = nombres;
        this.cantidades = cantidades;
        this.precios = precios;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View fila=view;
        LayoutInflater inflater = context.getLayoutInflater();
        if(view==null)
            fila = inflater.inflate(R.layout.fila_mejora, null, true);
        ImageView foto = (ImageView) fila.findViewById(R.id.foto);
        TextView nombre = (TextView) fila.findViewById(R.id.nombre);
        TextView cantidad = (TextView) fila.findViewById(R.id.cantidad);
        Button boton = (Button) fila.findViewById(R.id.botonComprar);

        foto.setImageResource(imagenes[position]);
        nombre.setText(nombres[position]);
        cantidad.setText("+" + cantidades[position] + "ox");
        boton.setText("PRECIO:\n" + precios[position] + " ox");

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oxi.hayOxigeno(precios[position])){
                    oxi.quitarOxigeno(precios[position]);
                    oxi.sumarOxiSegundo(cantidades[position]);
                }
            }
        });
        return  fila;
    }
}
