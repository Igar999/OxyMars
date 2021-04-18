package com.example.entrega1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogoImagenPerfil extends Dialog {

    private Activity padre;

    /**
     * Constructora del diálogo
     * @param context El contexto de la actividad donde se muestra el diálogo
     */
    public DialogoImagenPerfil(@NonNull Context context) {
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
        setContentView(R.layout.dialogo_imagen_perfil);

        Button botonCamara = findViewById(R.id.botonCamara);
        Button botonGaleria = findViewById(R.id.botonGaleria);
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent elIntentFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                padre.startActivityForResult(elIntentFoto, 70);
                dismiss();
            }
        });

        botonGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent elIntentGal = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                padre.startActivityForResult(elIntentGal, 64);
                dismiss();
            }
        });
    }
}
