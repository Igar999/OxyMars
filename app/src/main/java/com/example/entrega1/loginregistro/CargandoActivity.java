package com.example.entrega1.loginregistro;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.entrega1.juego.MainActivity;
import com.example.entrega1.R;
import com.example.entrega1.basedatos.ObtenerDatosUsuario;
import com.example.entrega1.basedatos.ReceptorResultados;

public class CargandoActivity extends AppCompatActivity {

    /**
     * Mientras se obtiene la información del servidor, se pone esta pantalla, para evitar que se cargue la pantalla principal sin datos.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando);

        //Se establece una animación, para que sea más vistoso
        ObjectAnimator animation = ObjectAnimator.ofFloat(findViewById(R.id.coheteCarga), "translationY", -1000f);
        animation.setDuration(2000);
        animation.start();

        //Se lanza la petición para obtener los datos de usuario y se espera la respuesta para redirigir a la ventana principal
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