package com.example.entrega1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.entrega1.runnables.BorrarUsuario;

import java.io.OutputStreamWriter;

public class DialogoMusica extends Dialog {

    private Activity padre;

    /**
     * Constructora del diálogo
     * @param context El contexto de la actividad donde se muestra el diálogo
     */
    public DialogoMusica(@NonNull Context context) {
        super(context);
        this.padre = (Activity)context;
    }

    /**
     * Se crea el diálogo, asignando las acciones correspondientes a cada botón mediante listeners. Si dice que sí, se accede a la base de datos para borrar el usuario y a la pantalla de login se le pasa el nombre de usuario, para borrarlo del fichero de usuarios y contraseñas
     * @param savedInstanceState
     */
    //https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialogo_musica);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(padre);
        builder.setTitle("Choose an animal");

// add a list
        String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // horse
                    case 1: // cow
                    case 2: // camel
                    case 3: // sheep
                    case 4: // goat
                }
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
