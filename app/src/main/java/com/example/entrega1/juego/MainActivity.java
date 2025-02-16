package com.example.entrega1.juego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entrega1.Actividad;
import com.example.entrega1.Oxigeno;
import com.example.entrega1.R;
import com.example.entrega1.Utils;
import com.example.entrega1.ajustes.AjustesActivity;
import com.example.entrega1.ajustes.DialogoSalir;
import com.example.entrega1.mapa.MapaActivity;
import com.example.entrega1.basedatos.ActualizarDatosUsuario;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Actividad {

    Oxigeno oxi = Oxigeno.getOxi();
    Utils utils = Utils.getUtils();
    String usuario = "";
    Handler handler = new Handler();
    Runnable[] listaRun = new Runnable[3];

    /**
     * Destruye los handlers, para evitar que se queden activos y al crear de nuevo la actividad se creen otros nuevos y estos sigan funcionando
     */
    //https://stackoverflow.com/questions/22718951/stop-handler-postdelayed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(listaRun[0]);
        handler.removeCallbacks(listaRun[1]);
        handler.removeCallbacks(listaRun[2]);
    }

    /**
     * Guarda los datos, para poder seguir el juego desde el mismo punto la próxima vez
     */
    @Override
    protected void onPause() {
        super.onPause();
        ActualizarDatosUsuario guardarDatos = new ActualizarDatosUsuario(usuario, oxi.getOxigeno(), oxi.getOxiToque(), oxi.getOxiSegundo(), oxi.getDesbloqueadoToque(), oxi.getDesbloqueadoSegundo());
        new Thread(guardarDatos).start();
        //guardarDatos(usuario); //BD LOCAL
    }

    /**
     * Se explica en el código con comentarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se establece el idioma antes de cargar la interfaz, para que se ponga en el idioma correcto al abrir la aplicación
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Locale nuevaloc = new Locale(prefs.getString("lista_idioma", "es"));
        Locale.setDefault(nuevaloc);
        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);
        Context context = createConfigurationContext(configuration);
        getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se obtiene el nombre del usuario, que se obtiene de la actividad del login, y se cargan de la base de datos sus estadísticas, para colocarlas en la interfaz
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
            Utils.getUtils().setUsuario(usuario);
        }

        //Se establece el contexto para varios métodos de la clase Oxigeno y Utils
        oxi.setContext(this);
        utils.setContext(context);

        //Se empieza la música si es necesario. Si se acaba de abrir se creará un nuevo MediaPlayer, si se llega desde otra actividad se reproducirá el ya existente
        if(Utils.getUtils().musicaLista()){
            Utils.getUtils().musicaPlay();
        }else{
            Utils.getUtils().empezarMusica(this);
        }

        //Se establece el fondo según las preferencias
        setFondo();

        //Se actualiza la interfaz para mostrar las mejoras compradas. Aparecen imagenes en el fondo y el planeta cambia para tener más cosas dentro
        oxi.actualizarInterfaz(this);

        //Se obtienen los elementos de la interfaz que se van a utilizar
        TextView textOxigeno = findViewById(R.id.oxigeno);
        TextView textOxiSegundo = findViewById(R.id.textOxiSegundo);
        TextView textOxiToque = findViewById(R.id.textOxiToque);
        ImageView planeta = findViewById(R.id.planeta);

        //Se esconden o muestan los fragments de mejoras dependiendo de la orientación. Si es vertical, no se muestra ninguno, si es horizontal se muestra uno
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),this);
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),this);
        }else{
            utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),this);
            utils.showFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),this);
        }

        //Establecer el handler y ls runnables para ejecutar acciones cada X tiempo
        //https://stackoverflow.com/questions/11434056/how-to-run-a-method-every-x-seconds

        //Se establece el runnable que hace que cada segundo se sume la cantidad correspondiente al oxígeno por segundo y se actualice la cantidad en la interfaz
        Runnable aumentarOxi = new Runnable() {
            public void run() {
                oxi.aumentarOxigenoSegundo();
                textOxigeno.setText(oxi.ponerCantidad(oxi.getOxigeno(), false));
                handler.postDelayed(this, 1000);
            }
        };
        listaRun[0] = aumentarOxi;

        //Se establece el runnable que se encarga de la animación de que el planeta rote sobre sí mismo. Adicionalmente, se guardan los datos del usuario en la base de datos
        //https://stackoverflow.com/questions/14039265/how-to-rotate-a-drawable-by-objectanimator
        Runnable general = new Runnable() {
            public void run() {
                //guardarDatos(usuario);

                //Se actualizan los datos del usuario en el servidor
                ActualizarDatosUsuario guardarDatos = new ActualizarDatosUsuario(usuario, oxi.getOxigeno(), oxi.getOxiToque(), oxi.getOxiSegundo(), oxi.getDesbloqueadoToque(), oxi.getDesbloqueadoSegundo());
                new Thread(guardarDatos).start();

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

        //Se establece el runnable que actualiza los textos de oxígeno, oxígeno por toque y oxígeno por segundo, de modo que siempre muestre el valor correcto
        Runnable actualizarTextos = new Runnable() {
            public void run() {
                textOxigeno.setText(oxi.ponerCantidad(oxi.getOxigeno(), false));
                textOxiToque.setText(oxi.ponerCantidad(oxi.getOxiToque(), false));
                textOxiSegundo.setText(oxi.ponerCantidad(oxi.getOxiSegundo(), false));
                handler.postDelayed(this, 100);
            }
        };
        listaRun[2] = actualizarTextos;

        //Se establece el listener en el planeta para que tenga animación de hacerse ligeramente más grande y se encoja acto seguido cada vez que se toca sobre él.
        //Además, se reproduce un sonido y se suma a la cantidad de oxñigeno lo correspondiente a la cantidad de oxígeno por toque.
        //https://developer.android.com/reference/android/view/animation/ScaleAnimation
        planeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oxi.aumentarOxigenoToque();
                textOxigeno.setText(oxi.ponerCantidad(oxi.getOxigeno(), false));
                utils.reproducirSonido(MainActivity.this, R.raw.planeta);
                ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.025f, 1.0f,
                        1.025f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnim.setDuration(75);
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
                        scaleAnim.setFillEnabled(true);
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

        //Se le asigna el listener al botón de mejoras de oxígeno por toque para que al pulsarlo se muestren u oculten los fragmentos que correspondan (Solo se muestra en vertical)
        Button botonToque = findViewById(R.id.botonToque);
        botonToque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
                utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);

            }
        });

        //Se le asigna el listener al botón de mejoras de oxígeno por segundo para que al pulsarlo se muestren u oculten los fragmentos que correspondan (Solo se muestra en vertical)
        Button botonSegundo = findViewById(R.id.botonSegundo);
        botonSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);
                utils.hideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
            }
        });

        //Se le asigna el listener al botón de cambio de listado de mejoras para que al pulsarlo se esconda una lista y se muestre la otra. (Solo se muestra en horizontal)
        Button botonCambio = findViewById(R.id.botonCambio);
        botonCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasSegundo),MainActivity.this);
                utils.showHideFragment(getFragmentManager().findFragmentById(R.id.mejorasToque),MainActivity.this);
                cambiarBotonCambio();
            }
        });

        //Se le asigna el listener al botón de ajustes para que reproduzca un sonido y lleve a la pantalla de ajustes
        ImageView botonAjustes = findViewById(R.id.botonAjustes);
        botonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.reproducirSonido(MainActivity.this, R.raw.ajustes);
                Intent i = new Intent(MainActivity.this, AjustesActivity.class);
                i.putExtra("usu", usuario);
                startActivity(i);
                finish();
            }
        });

        //Se le asigna el listener al botón de mapa para que reproduzca un sonido y lleve a la pantalla de mapa
        ImageView botonMapa = findViewById(R.id.botonMapa);
        botonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.reproducirSonido(MainActivity.this, R.raw.ajustes);
                Intent i = new Intent(MainActivity.this, MapaActivity.class);
                i.putExtra("usu", usuario);
                startActivity(i);
                finish();
            }
        });

        //Se le asigna el lostener al botón de música para que compruebe permisos de acceso a los archivos y si los tiene cree el diálogo de selección de canción
        ImageView botonMusica = findViewById(R.id.botonMusica);
        botonMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobar permisos
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 50);
                }else{
                    //Crear diálogo
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getResources().getString(R.string.elige_cancion));
                    HashMap<String,String> listaCanciones = Utils.getUtils().obtenerCancionesDispositivo();
                    String[] canciones = new String[listaCanciones.entrySet().size()+1];
                    canciones[0] = "OxyMars";
                    int cont = 1;
                    for (Map.Entry<String, String> par : listaCanciones.entrySet()){
                        canciones[cont] = par.getKey();
                        cont++;
                    }
                    builder.setItems(canciones, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indice) {
                            String seleccion = canciones[indice];
                            //Si se elige la canción predefinida de OxyMars, reproducir esa
                            if (seleccion.equals("OxyMars")){
                                Utils.getUtils().cambiarMusica(MainActivity.this, null);
                            }else{ //Si se elige otra, reproducirla desde su ruta
                                Utils.getUtils().cambiarMusica(MainActivity.this, listaCanciones.get(seleccion));
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        //Se le asigna el listener al botón de ranking para que lleve a la pantalla de ranking
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

        //Se lanzan los tres runnables creados previamente
        handler.postDelayed(listaRun[0], 1000);
        handler.postDelayed(listaRun[2], 100);
        handler.postDelayed(listaRun[1], 500);
    }

    /**
     * Se establece como fondo la imagen que dicen las preferencias
     */
    private void setFondo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String num = prefs.getString("lista_fondo", "1");
        String ori = "land";
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ori = "por";
        }
        String nombre = "espacio"+num+ori;

        int id = MainActivity.this.getResources().getIdentifier(nombre, "drawable", MainActivity.this.getPackageName());
        findViewById(R.id.layout).setBackground(getDrawable(id));
    }

    /**
     * Al pulsar el botón "atrás" se esconde el fragment que estuviera mostrandose, y si no hay ninguno, muestra el diálogo para confirmar que se quiere salir de la aplicación
     * En horizontal muesta el diálogo siempre que se pulsa, ya que siempre hay un fragment en pantalla
     */
    @Override
    public void onBackPressed() {
        utils.reproducirSonido(this, R.raw.atras);
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        if((getFragmentManager().findFragmentById(R.id.mejorasSegundo).isHidden() && getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()) || this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            Dialog dialogocerrar= new DialogoSalir(this);
            dialogocerrar.show();
        } else if (!getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()) {
            ft.hide(getFragmentManager().findFragmentById(R.id.mejorasToque));
            ft.commit();
        } else if (!getFragmentManager().findFragmentById(R.id.mejorasSegundo).isHidden()) {
            ft.hide(getFragmentManager().findFragmentById(R.id.mejorasSegundo));
            ft.commit();
        }
    }


    /**
     * Solo se usa en horizontal, cambia el texto que aparece en el botón que cambia entre los dos listados de mejoras
     */
    public void cambiarBotonCambio(){
        if (getFragmentManager().findFragmentById(R.id.mejorasToque).isHidden()) {
            ((Button)findViewById(R.id.botonCambio)).setText(getString(R.string.mejoras_oxi_segundo));
        } else {
            ((Button)findViewById(R.id.botonCambio)).setText(getString(R.string.mejoras_oxi_toque));
        }
    }

//    /**
//     * Accede a la base de datos local para obtener el oxígeno, el oxígeno por toque, el oxígeno por segundo y las mejoras desbloqueadas de cada tipo para el usuario indicado, y los almacena en la clase Oxigeno
//     * Si el usuario no está en la base de datos, se almacenan en la clase Oxigeno unos valores por defecto
//     * @param usuario El nombre del usuario actual
//     */
    /*public void cargarDatos(String usuario){
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
    }*/

//    /**
//     * Accede a la base de datos para modificar el registro del jugador cuyo nombre se pasa como parámetro y almacenar las estadísticas actuales
//     * Si el usuario no existe (porque es la primera vez que juega tras crear su usuario), se crea un nuevo registro y se insertan las estadísticas actuales
//     * @param usuario El nombre del usuario actual
//     */
    /*public void guardarDatos(String usuario){
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
    }*/

    /**
     * Si recibe un resultado de petición de permiso de código 50, obtiene la lista de canciones y crea el Dialog para seleccionar una, y se reproduce la seleccionada
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 50: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getResources().getString(R.string.elige_cancion));
                        //Obtener canciones
                        HashMap<String,String> listaCanciones = Utils.getUtils().obtenerCancionesDispositivo();
                        String[] canciones = new String[listaCanciones.entrySet().size()+1];
                        canciones[0] = "OxyMars";
                        int cont = 1;
                        for (Map.Entry<String, String> par : listaCanciones.entrySet()){
                            canciones[cont] = par.getKey();
                            cont++;
                        }
                        //Crear la lista en la interfaz con las canciones
                        builder.setItems(canciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indice) {
                                //Reproducir la canción seleccionada
                                String seleccion = canciones[indice];
                                if (seleccion.equals("OxyMars")){
                                    Utils.getUtils().cambiarMusica(MainActivity.this, null);
                                }else{
                                    Utils.getUtils().cambiarMusica(MainActivity.this, listaCanciones.get(seleccion));
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        }
    }
}