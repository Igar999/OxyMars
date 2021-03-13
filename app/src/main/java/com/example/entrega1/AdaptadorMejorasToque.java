package com.example.entrega1;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AdaptadorMejorasToque extends ArrayAdapter {
    private Integer[] imagenes;
    private String[] nombres;
    private float[] cantidades;
    private float[] precios;
    private Activity context;
    private Oxigeno oxi = Oxigeno.getOxi();
    private Utils utils = Utils.getUtils();

    public AdaptadorMejorasToque(Activity context, Integer[] imagenes, String[] nombres, float[] cantidades, float[] precios) {
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
        cantidad.setText("+" + oxi.ponerCantidad(cantidades[position], true)+ "ox");
        boton.setText(context.getString(R.string.precio) + ":\n" + oxi.ponerCantidad(precios[position], true) + " ox");
        if (position <= oxi.getDesbloqueadoToque()){
            fila.setVisibility(View.VISIBLE);
        }
        else{
            fila.setVisibility(View.INVISIBLE);
        }

        final View filaFin = fila;
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oxi.hayOxigeno(precios[position])){
                    oxi.quitarOxigeno(precios[position]);
                    oxi.sumarOxiToque(cantidades[position]);
                    if(parent.indexOfChild(filaFin) != parent.getChildCount()-1){
                        parent.getChildAt(parent.indexOfChild(filaFin)+1).setVisibility(View.VISIBLE);
                    }

                    if(position == oxi.getDesbloqueadoSegundo()){
                        utils.reproducirSonido(getContext(), R.raw.primerdesbloqueo);
                    }
                    else{
                        utils.reproducirSonido((Activity)getContext(), R.raw.comprar);
                    }

                    oxi.desbloquearToque(position+1);
                    oxi.actualizarInterfaz((Activity)getContext());
                }
            }
        });
        return  fila;
    }
}
