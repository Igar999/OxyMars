package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AyudaActivity extends AppCompatActivity {

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
        }
        TextView texto = findViewById(R.id.textoAyuda);
        texto.setText("OxyMars consiste en conseguir oxígeno para crear vida en Marte.\n\n" +
                "Cada vez que se toca el planeta, se genera oxígeno, que se puede ver en la parte superior de la pantalla.\n\n" +
                "Existen dos tipos de mejoras:\n" +
                "   - Mejoras de oxígeno por toque: Hacen que al tocar el planeta se genere mayor cantidad de oxígeno.\n" +
                "   - Mejoras de oxígeno por segundo: Hacen que cada segundo se obtenga mayor cantidad de oxígeno aunque no se toque el planeta.\n\n" +
                "Existe un ranking, en el que se pueden ver la puntuación propia y la del resto de usuarios registrados.\n\n");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AyudaActivity.this, AjustesActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }
}