package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class RankingActivity extends Actividad {

    private String usuario;

    /**
     * Se explica en el código con comentarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        //Se obtiene el usuario actual, para marcarlo de manera distinta en el ranking
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
        }

        //Se accede a la base de datos para obtener los datos de todos los usuarios y meterlos en listas
        GuardarDatos gestorDB = new GuardarDatos (this, "OxyMars", null, 1);
        SQLiteDatabase bd = gestorDB.getWritableDatabase();
        Cursor cu = bd.rawQuery("SELECT Usuario,Oxigeno FROM Datos ORDER BY Oxigeno DESC", null);
        String[] listaUsu = new String[0];
        float[] listaCant = new float[0];
        int[] listaPos = new int[0];
        if(cu.getCount() != 0){
            listaUsu = new String[cu.getCount()];
            listaCant = new float[cu.getCount()];
            listaPos = new int[cu.getCount()];
            cu.moveToFirst();
            for(int i = 0; i < cu.getCount(); i++){
                listaPos[i] = i+1;
                listaUsu[i] = cu.getString(0);
                listaCant[i] = cu.getFloat(1);
                cu.moveToNext();
            }
        }

        //Se crea la cabecera de la lista y el adaptador de la lista
        ListView rank = (ListView) findViewById(R.id.ranking);
        TextView titulo = new TextView(this);
        titulo.setText(getString(R.string.ranking));
        titulo.setTypeface(Typeface.DEFAULT_BOLD);
        titulo.setTextColor(Color.WHITE);
        titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
        titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rank.addHeaderView(titulo);
        AdaptadorRanking adaptador= new AdaptadorRanking(this,listaUsu,listaCant,listaPos,usuario);
        rank.setAdapter(adaptador);
    }

    /**
     * Al pulsar el botón "atrás", se reproduce un sonido y se vuelve a la actividad principal
     */
    @Override
    public void onBackPressed() {
        Utils.getUtils().reproducirSonido(this, R.raw.atras);
        Intent i = new Intent(RankingActivity.this, MainActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }
}