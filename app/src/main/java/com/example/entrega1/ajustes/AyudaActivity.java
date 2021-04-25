package com.example.entrega1.ajustes;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.entrega1.Actividad;
import com.example.entrega1.R;
import com.example.entrega1.Utils;

public class AyudaActivity extends Actividad {

    private String usuario;

    /**
     * Se muestra la ventana y se pone un scroll en el texto, por si es demasiado largo. También se obtiene el usuario
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
        }
        TextView texto = findViewById(R.id.textoAyuda);
        texto.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * Al pulsar el botón "atrás", se reproduce un sonido y se vuelve a la actividad de ajustes
     */
    @Override
    public void onBackPressed() {
        Utils.getUtils().reproducirSonido(this, R.raw.atras);
        Intent i = new Intent(AyudaActivity.this, AjustesActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }
}