package com.example.entrega1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.MissingFormatArgumentException;

public class DialogoSalir extends Dialog {

    private Activity padre;

    /**
     * Constructora del diálogo
     * @param context El contexto de la actividad donde se muestra el diálogo
     */
    public DialogoSalir(@NonNull Context context) {
        super(context);
        this.padre = (Activity)context;
    }

    /**
     * Se crea el diálogo, asignando las acciones correspondientes a cada botón mediante listeners
     * @param savedInstanceState
     */
    //https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_salir);

        Button botonSi = findViewById(R.id.botonSi);
        Button botonNo = findViewById(R.id.botonNo);
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                padre.finish();
            }
        });

        botonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
