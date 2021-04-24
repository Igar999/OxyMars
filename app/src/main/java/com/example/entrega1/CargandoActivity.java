package com.example.entrega1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.entrega1.runnables.ObtenerDatosUsuario;
import com.example.entrega1.runnables.ReceptorResultados;

public class CargandoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando);

        ObjectAnimator animation = ObjectAnimator.ofFloat(findViewById(R.id.coheteCarga), "translationY", -1000f);
        animation.setDuration(2000);
        animation.start();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String usuario = extras.getString("usu");
            ObtenerDatosUsuario cargarDatos = new ObtenerDatosUsuario(usuario);
            new Thread(cargarDatos).start();
            Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    if (ReceptorResultados.getReceptorResultados().haAcabadoCargar()){
                        Intent i = new Intent(CargandoActivity.this, MainActivity.class);
                        i.putExtra("usu", usuario);
                        startActivity(i);
                        finish();
                    }else{
                        handler.postDelayed(this, 500);
                    }
                }
            };
            handler.postDelayed(run, 1000);
        }
    }
}