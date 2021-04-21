package com.example.entrega1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.entrega1.runnables.BorrarUsuario;

import java.io.OutputStreamWriter;

public class DialogoBrote extends Dialog {

    /**
     * Constructora del diálogo
     * @param context El contexto de la actividad donde se muestra el diálogo
     */
    public DialogoBrote(@NonNull Context context) {
        super(context);
    }

    /**
     * Se crea el diálogo
     * @param savedInstanceState
     */
    //https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_brote);

        TextView texto = findViewById(R.id.textoBrote);
        texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoBrote.this.dismiss();
            }
        });
    }
}
