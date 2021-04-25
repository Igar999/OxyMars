package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.entrega1.notificaciones.ServicioFirebase;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    private static Utils utils;
    private MediaPlayer musica;
    private Context context;
    private Boolean musicaLista = false;
    private String usuario;
    private String imagenUsu;
    private Integer pasos = -1;
    private ArrayList<Marker> listaMarkers = new ArrayList<Marker>(5);

    /**
     * Crea una instancia de la clase o devuelve la que ya existe
     * @return Instancia de la clase Utils (Singleton)
     */
    public static Utils getUtils() {
        if (utils == null) {
            utils = new Utils();
        }
        return utils;
    }

    /**
     * Constructora de la clase
     */
    private Utils(){
    }

    /**
     * Almacena el contexto, para poder utilizarlo en métodos que requieren de contexto, pero que se usan en varias clases distintas
     * @param context
     */
    public void setContext(Context context){
        this.context = context;
    }


    /**
     * Pasa un fragment de visible a invisible, y viceversa
     * @param fragment El fragment cuya visibilidad se quiere cambiar
     * @param actividad La actividad en la que está el fragment
     */
    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void showHideFragment(final Fragment fragment, AppCompatActivity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (fragment.isHidden()) {
            ft.show(fragment);
        } else {
            ft.hide(fragment);
        }
        ft.commit();
    }

    /**
     * Esconde un fragment que está visible
     * @param fragment El fragment a esconder
     * @param actividad La actividad en la que está el fragment
     */
    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void showFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (fragment.isHidden()) {
            ft.show(fragment);
        }
        ft.commit();
    }

    /**
     * Muestra un fragment que está invisible
     * @param fragment El fragment a mostrar
     * @param actividad La actividad en la que está el fragment
     */
    //https://www.semicolonworld.com/question/47971/show-hide-fragment-in-android (respuesta de kishan patel)
    public void hideFragment(final Fragment fragment, Activity actividad){
        FragmentTransaction ft = actividad.getFragmentManager().beginTransaction();
        if (!fragment.isHidden()) {
            ft.hide(fragment);
        }
        ft.commit();
    }

    /**
     * Reproduce la musica almacenada en la variable si no está sonando y el usuario ha puesto en las preferencias que quiere música de fondo
     */
    public void musicaPlay(){
        if (musica != null){
            if (!musica.isPlaying() && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("musica", true)){
                musica.start();
            }
        }
    }

    /**
     * Pausa la música almacenada en la variable si está sonando
     */
    public void musicaPause(){
        if (musica.isPlaying()){
            musica.pause();
        }
    }

    /**
     * Almacena el MediaPlayer en la variable musica para poder acceder a ella en el futuro, y la empieza
     * @param context
     */
    public void empezarMusica(Context context){
        if (musica == null){
            musica = MediaPlayer.create(context, R.raw.musicafondo);
            musica.setLooping(true); // Set looping
            musicaPlay();
            musicaLista = true;
        }
    }

    /**
     * Para la canción que estaba sonando previamente y empieza una nueva canción
     * @param context La actividad desde la que se lanza el método
     * @param ruta La ruta en el dispositivo de la canción que se va a poner. Si es null, se reproduce la canción de fondo predeterminada
     */
    public void cambiarMusica(Context context, String ruta){
        musicaPause();
        musica.release();
        try {
            //Si es null, se pone la canción por defecto
            if (ruta == null){
                musica = MediaPlayer.create(context, R.raw.musicafondo);
                musica.setLooping(true); // Set looping
                musicaPlay();
                musicaLista = true;
            } else{ //Si no es null, se reproduce la canción de la ruta
                musica = new MediaPlayer();
                musica.setDataSource(ruta);
                musica.prepare();
                musica.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        musica.setLooping(true);
                        musicaPlay();
                        musicaLista = true;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve true si la musica está preparada, y false si tiene que crearse aún el MediaPlayer
     * @return Booleano que indica si está lista la música
     */
    public Boolean musicaLista(){
        return this.musicaLista;
    }

    /**
     * Reproduce un sonido si los sonidos están activados en las preferencias
     * @param context El contexto de la actividad en la que se va a reproducir el sonido
     * @param sonido El sonido a reproducir
     */
    //https://stackoverflow.com/questions/42326228/playing-short-sound-on-touch
    public void reproducirSonido(Context context, int sonido){
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("sonido",true)){
            MediaPlayer mediaPlayer = MediaPlayer.create(context,sonido);
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(1,1);
                    mp.start();
                }

            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
    }

    /**
     * Devuelve el usuario, que se almacena en una variable local
     * @return El nombre de usuario
     */
    public String getUsuario(){
        return this.usuario;
    }

    /**
     * Almacena el nombre de usuario, para poder acceder a él en el futuro
     * @param nombre El nombre a establecer como nombre de usuario
     */
    public void setUsuario(String nombre){
        this.usuario = nombre;
    }

    /**
     * Se almacena la imagen del usuario en String
     * @param foto El string de la foto de usuario
     */
    public void imagenUsuario(String foto) {
        this.imagenUsu = foto;
    }

    /**
     * Se obtiene la imagen de usuario en String
     * @return El string de la foto de usuario
     */
    public String getImagenUsu() {
        return imagenUsu;
    }

    /**
     * Se lanza un mensaje al php del servidor para que se lance un mensaje Firebase
     * @param context La actividad desde la que se lanza el método
     * @param mensaje El mensaje que se envía al servidor. Dependiendo de esto, se recibirá una notificación u otra.
     */
    public void enviarFCM(Context context, String mensaje){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("token", ServicioFirebase.getToken(context))
                            .appendQueryParameter("mensaje", mensaje);
                    String parametros = builder.build().getEncodedQuery();

                    String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/fcm.php";
                    HttpURLConnection urlConnection = null;
                    URL destino = new URL(direccion);
                    urlConnection = (HttpURLConnection) destino.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(parametros);
                    out.close();

                    int statusCode = urlConnection.getResponseCode();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * Comprueba si previamente había un usuario logeado al cerrar la app, para que no tenga que logearse de nuevo
     * @return nombre del usuario logeado (vacío si no hay)
     */
    public String comprobarUsuarioLogeado(Context contexto) {
        String usuarioLog = "";
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(contexto.openFileInput("usuLog.txt")));
            usuarioLog = ficherointerno.readLine();
            if (usuarioLog == null){
                usuarioLog = "";
            }
            ficherointerno.close();
        } catch (Exception e){
            e.printStackTrace();
            try{
                OutputStreamWriter fichero = new OutputStreamWriter(contexto.openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
                fichero.write("");
                fichero.close();
                usuarioLog = "";
            } catch (Exception f){
                f.printStackTrace();
            }
        }
        return usuarioLog;
    }

    /**
     * Se almacenan los pasos que ha dado el usuario durante el día cuando se abre la aplicación, para poder contar a partir de ahí.
     * @param pasos Los pasos dados durante el día
     */
    public void setPasos(Integer pasos) {
        if (this.pasos == -1){
            this.pasos = pasos;
        }
    }

    /**
     * Se comprueba si ha dado 20 pasos, y si es así, se suma oxígeno
     * @param nuevosPasos El nuevo valor de los pasos dados durante el día
     */
    public void comprobarPasos(Integer nuevosPasos) {
        if ((nuevosPasos - pasos) >= 20){
            pasos = pasos + 20;
            Oxigeno.getOxi().aumentarOxigenoToque();
        }
    }

    /**
     * Devuelve la lista de marcadores de brote para el mapa
     * @return La lista de marcadores
     */
    public ArrayList<Marker> getListaMarkers(){
        return listaMarkers;
    }

    /**
     * Almacena la lista de marcadores de brote del mapa, para que si se sale del mapa y se vuelve a entrar, no se vuelvan a generar aleatoriamente
     * @param listaMarkers La lista de marcadores
     */
    public void setListaMarkers(ArrayList<Marker> listaMarkers) {
        this.listaMarkers = listaMarkers;
    }

    /**
     * Obtiene el listado de audios que hay en el almacenamiento externo del dispositivo
     * @return Lista de canciones en formato (título, ruta)
     */
    //https://stackoverflow.com/questions/35872207/unable-to-get-audio-list-from-content-provider
    public HashMap obtenerCancionesDispositivo(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 93);
        } else {
            HashMap<String,String> lista = new HashMap<>();
            Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = context.getContentResolver().query(musicUri,null, null, null,null);
            musicCursor.moveToFirst();
            for(int i=0;i<musicCursor.getCount();i++){
                lista.put(musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                musicCursor.moveToNext();
            }
            return lista;
        }
        return null;
    }
}
