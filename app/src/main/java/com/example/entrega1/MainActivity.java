package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Oxigeno oxi = Oxigeno.getOxi();
    Utils utils = Utils.getUtils();
    String usuario = "";
    Handler handler = new Handler();
    Runnable[] listaRun = new Runnable[3];

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Lanzar notificación
        //https://developer.android.com/training/scheduling/alarms
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5 * 1000, alarmIntent);

        handler.removeCallbacks(listaRun[0]);
        handler.removeCallbacks(listaRun[1]);
        handler.removeCallbacks(listaRun[2]);

    }

    @Override
    protected void onPause() {
        super.onPause();
        guardarDatos(usuario);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
            cargarDatos(usuario);
        }

        setFondo();

        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionProgramada.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmMgr.cancel(alarmIntent);

        TextView textOxigeno = findViewById(R.id.oxigeno);
        TextView textOxiSegundo = findViewById(R.id.textOxiSegundo);
        TextView textOxiToque = findViewById(R.id.textOxiToque);
        ImageView planeta = findViewById(R.id.planeta);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),this);
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),this);
        }else{
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),this);
            utils.showFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),this);
        }

        Runnable aumentarOxi = new Runnable() {
            public void run() {
                oxi.aumentarOxigenoSegundo();
                textOxigeno.setText(ponerCantidad(oxi.getOxigeno()));
                handler.postDelayed(this, 1000);
            }
        };
        listaRun[0] = aumentarOxi;
        handler.postDelayed(listaRun[0], 1000);


        Runnable general = new Runnable() {
            public void run() {
                guardarDatos(usuario);
                int i = planeta.getMeasuredHeight();
                int j = planeta.getMeasuredWidth();
                ObjectAnimator animation = ObjectAnimator.ofFloat(planeta, "rotation", 0, 360);
                animation.setDuration(45000);
                planeta.setPivotX(i/2);
                planeta.setPivotY(j/2);
                animation.setRepeatCount(ObjectAnimator.INFINITE);
                animation.setInterpolator(new LinearInterpolator());
                animation.start();
                handler.postDelayed(this, 45000);
            }
        };
        listaRun[1] = general;
        handler.postDelayed(listaRun[1], 250);


        Runnable actualizarTextos = new Runnable() {
            public void run() {
                textOxigeno.setText(ponerCantidad(oxi.getOxigeno()));
                textOxiToque.setText(ponerCantidad(oxi.getOxiToque()));
                textOxiSegundo.setText(ponerCantidad(oxi.getOxiSegundo()));
                handler.postDelayed(this, 100);
            }
        };
        listaRun[2] = actualizarTextos;
        handler.postDelayed(listaRun[2], 100);

        planeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(planeta, "translationY", -200f);
                animation.setDuration(2000);
                animation.start();*/
                oxi.aumentarOxigenoToque();
                textOxigeno.setText(ponerCantidad(oxi.getOxigeno()));
                ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.025f, 1.0f,
                        1.025f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnim.setDuration(75);
                // scaleAnim.setFillEnabled(true);
                scaleAnim.setFillAfter(true);
                scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ScaleAnimation scaleAnim2 = new ScaleAnimation(1.025f, 1.0f, 1.025f,
                                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnim2.setDuration(75);
                        // scaleAnim.setFillEnabled(true);
                        scaleAnim2.setFillAfter(true);
                        planeta.setAnimation(scaleAnim2);
                        planeta.startAnimation(scaleAnim2);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                planeta.setAnimation(scaleAnim);
                planeta.startAnimation(scaleAnim);
            }
        });

        Button botonToque = findViewById(R.id.botonToque);
        botonToque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
                utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);
                //getFragmentManager().beginTransaction().add(R.id.layout, new MejorasToque(), "mejorasToque");

                /*Fragment f = getFragmentManager().findFragmentByTag("mejoras");
                if(f!=null) getFragmentManager().beginTransaction().remove(f);
                getFragmentManager().beginTransaction().commit();*/

            }
        });
        Button botonSegundo = findViewById(R.id.botonSegundo);
        botonSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);
                utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
                //getFragmentManager().beginTransaction().add(R.id.layout, new MejorasToque(), "mejorasToque");

                /*Fragment f = getFragmentManager().findFragmentByTag("mejoras");
                if(f!=null) getFragmentManager().beginTransaction().remove(f);
                getFragmentManager().beginTransaction().commit();*/

            }
        });
        Button botonCambio = findViewById(R.id.botonCambio);
        botonCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
                cambiarBotonCambio();
                //getFragmentManager().beginTransaction().add(R.id.layout, new MejorasToque(), "mejorasToque");

                /*Fragment f = getFragmentManager().findFragmentByTag("mejoras");
                if(f!=null) getFragmentManager().beginTransaction().remove(f);
                getFragmentManager().beginTransaction().commit();*/
            }
        });

        ImageView botonAjustes = findViewById(R.id.botonAjustes);
        botonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AjustesActivity.class);
                i.putExtra("usu", usuario);
                startActivity(i);
                finish();
            }
        });

        Button ranking = findViewById(R.id.botonRanking);
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RankingActivity.class);
                i.putExtra("usu", usuario);
                startActivity(i);
                finish();
            }
        });

    }

    private void setFondo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String num = prefs.getString("lista_fondo", "1");
        String ori = "land";
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ori = "por";
        }
        String nombre = "espacio"+num+ori;

        int id = MainActivity.this.getResources().getIdentifier(nombre, "drawable", MainActivity.this.getPackageName());
        findViewById(R.id.layout).setBackground(getDrawable(id));
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        if (!getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()) {
            ft.hide(getFragmentManager().findFragmentById(R.id.mejorasToque));
            ft.commit();
        }
        if (!getFragmentManager().findFragmentById(R.id.mejorasSegundo).isHidden()) {
            ft.hide(getFragmentManager().findFragmentById(R.id.mejorasSegundo));
            ft.commit();
        }
        if(getFragmentManager().findFragmentById(R.id.mejorasSegundo).isHidden() && getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()){
            DialogFragment dialogocerrar= new DialogoSalir();
            dialogocerrar.show(getSupportFragmentManager(), "etiqueta");
        }
    }

    public void cambiarBotonCambio(){
        if (getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()) {
            ((Button)findViewById(R.id.botonCambio)).setText("MEJORAS\nOXI/SEGUNDO");
        } else {
            ((Button)findViewById(R.id.botonCambio)).setText("MEJORAS\nOXI/TOQUE");
        }
    }

    public void cargarDatos(String usuario){
        GuardarDatos GestorDB = new GuardarDatos (this, "OxyMars", null, 1);
        SQLiteDatabase bd = GestorDB.getWritableDatabase();

        String[] campos = new String[] {"Usuario", "Oxigeno", "OxiToque", "OxiSegundo", "DesbloqueadoToque", "DesbloqueadoSegundo"};
        String[] argumentos = new String[] {usuario};
        Cursor cu = bd.query("Datos",campos,"Usuario=?",argumentos,null,null,null);
        if(cu.getCount() != 0){
            cu.moveToFirst();
            float oxigeno = cu.getFloat(1);
            float oxiToque = cu.getFloat(2);
            float oxiSegundo = cu.getFloat(3);
            int desbloqueadoToque = cu.getInt(4);
            int desbloqueadoSegundo = cu.getInt(5);
            Oxigeno.getOxi().cargarOxi(this,oxigeno,oxiToque,oxiSegundo,desbloqueadoToque,desbloqueadoSegundo);
        }
        else{
            Oxigeno.getOxi().cargarOxi(this,0,1,0,0,0);
        }
    }

    public void guardarDatos(String usuario){
        Oxigeno oxi = Oxigeno.getOxi();
        GuardarDatos gestorDB = new GuardarDatos (this, "OxyMars", null, 1);
        SQLiteDatabase bd = gestorDB.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("Usuario", usuario);
        valores.put("Oxigeno", oxi.getOxigeno());
        valores.put("OxiToque", oxi.getOxiToque());
        valores.put("OxiSegundo", oxi.getOxiSegundo());
        valores.put("DesbloqueadoToque", oxi.getDesbloqueadoToque());
        valores.put("DesbloqueadoSegundo", oxi.getDesbloqueadoSegundo());

        String[] campos = new String[] {"Usuario"};
        String[] argumentos = new String[] {usuario};

        Cursor cu = bd.query("Datos",campos,"Usuario=?",argumentos,null,null,null);
        if (cu.getCount() == 0){
            //INSERT
            bd.insert("Datos", null, valores);
        }
        else{
            //UPDATE
            bd.update("Datos", valores, "Usuario=?", argumentos);
        }
    }

    public String ponerCantidad(float cant){
        String texto = String.valueOf(cant);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
         if (prefs.getBoolean("notacion", true)){
             texto = oxi.ponerCantidad(cant);
         }
         return texto;
    }
}