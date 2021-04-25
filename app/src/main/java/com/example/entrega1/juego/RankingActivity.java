package com.example.entrega1.juego;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entrega1.Actividad;
import com.example.entrega1.R;
import com.example.entrega1.Utils;
import com.example.entrega1.basedatos.ObtenerUsuariosRanking;
import com.example.entrega1.basedatos.ReceptorResultados;

import org.json.JSONArray;
import org.json.JSONObject;

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

        //Se accede a la base de datos local para obtener los datos de todos los usuarios y meterlos en listas
        /*GuardarDatos gestorDB = new GuardarDatos (this, "OxyMars", null, 1);
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
        }*/



        //Se obtienen los datos de todos los jugadores del servidor
        ObtenerUsuariosRanking personas = new ObtenerUsuariosRanking();
        new Thread(personas).start();

        Handler handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                //Si los datos estan cargados, proseguir
                if (ReceptorResultados.getReceptorResultados().haAcabadoRanking()){
                    String[] listaUsu = new String[0];
                    float[] listaCant = new float[0];
                    int[] listaPos = new int[0];

                    //Se almacenan los datos de nombres, posiciones y puntuaciones en tres listas
                    ReceptorResultados.getReceptorResultados().setFinRanking(false);
                    try{
                        JSONArray array = ReceptorResultados.getReceptorResultados().obtenerResultadoRanking();
                        listaUsu = new String[array.length()];
                        listaCant = new float[array.length()];
                        listaPos = new int[array.length()];
                        for (int i = 0; i < array.length(); i++){
                            JSONObject j = array.getJSONObject(i);
                            listaPos[i] = i+1;
                            listaUsu[i] = j.getString("usuario");
                            listaCant[i] = j.getInt("oxigeno");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }


                    //Se crea la cabecera de la lista y el adaptador de la lista
                    ListView rank = (ListView) findViewById(R.id.ranking);
                    TextView titulo = new TextView(RankingActivity.this);
                    titulo.setText(getString(R.string.ranking));
                    titulo.setTypeface(Typeface.DEFAULT_BOLD);
                    titulo.setTextColor(Color.WHITE);
                    titulo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                    titulo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    rank.addHeaderView(titulo);
                    AdaptadorRanking adaptador= new AdaptadorRanking(RankingActivity.this,listaUsu,listaCant,listaPos,usuario);
                    rank.setAdapter(adaptador);
                }else{
                    handler.postDelayed(this, 200);
                }
            }
        };

        handler.postDelayed(run, 100);


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