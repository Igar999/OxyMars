package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entrega1.runnables.ComprobarExisteUsuario;
import com.example.entrega1.runnables.ComprobarUsuario;
import com.example.entrega1.runnables.CrearUsuario;
import com.example.entrega1.runnables.ReceptorResultados;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class LoginActivity extends AppCompatActivity {

    private Map<String,String> mapa;


    /**
     * Se explica en el código con comentarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se comprueba si ya hay algun usuario logeado, y si es el caso, se salta directamente al juego
        String usuario = Utils.getUtils().comprobarUsuarioLogeado(this);
        if (usuario != ""){
            jugar(usuario);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils utils = Utils.getUtils();

        //Se carga el json del fichero de usuarios y contraseñas a un HashMap, para poder acceder fácilmente a los datos
        //leerDeFichero();

        //Se obtienen los dos fragments
        Fragment login =  getFragmentManager().findFragmentById(R.id.fragmentLogin);
        Fragment registrar = getFragmentManager().findFragmentById(R.id.fragmentRegistrar);

        //Se comprueba si hay que borrar algún usuario (por si se ha dado a la opción de borrarlo desde el menú de ajustes)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("borrar") != null){
                mapa.remove(extras.getString("borrar"));
                escribirAFichero();
            }
        }

        //Si es vertical, se muestra el login, si es horizontal, se muestran ambos fragments
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            utils.hideFragment(registrar,this);
            utils.showFragment(login,this);
        }else{
            utils.showFragment(registrar,this);
            utils.showFragment(login,this);
        }

        //Se establece el listener para que el botón que cambia entre login y registro cambie su texto correctamente (solo en vertical)
        TextView cambio = findViewById(R.id.cambioLoginRegistrar);
        cambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(login,LoginActivity.this);
                utils.showHideFragment(registrar,LoginActivity.this);
                cambiarBotonCambio();
            }
        });

        //Se asigna el listener al botón para que compruebe en el HashMap si los datos de login son correctos, y si lo son, se va al juego. Si no, se muestra el error
        Button botonLogin = login.getActivity().findViewById(R.id.botonLogin);
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuarioLogin = login.getActivity().findViewById(R.id.textLoginUsuario);
                EditText contraLogin = login.getActivity().findViewById(R.id.textLoginContra);
                //Boolean estado = comprobarLogin(usuarioLogin.getText().toString(),contraLogin.getText().toString()); //BD LOCAL

                if (contraLogin.getText().toString().equals("")){
                    contraLogin.setError(getString(R.string.rellena_campo));
                }
                if (usuarioLogin.getText().toString().equals("")){
                    usuarioLogin.setError(getString(R.string.rellena_campo));
                }else{
                    String contraEnc = encriptar(contraLogin.getText().toString());
                    ComprobarUsuario comprobar = new ComprobarUsuario(usuarioLogin.getText().toString(), contraEnc);
                    new Thread(comprobar).start();
                    while (!ReceptorResultados.getReceptorResultados().haAcabadoUsuario()){
                        ;
                    }

                    Handler handler = new Handler();
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            if (ReceptorResultados.getReceptorResultados().haAcabadoUsuario()) {
                                ReceptorResultados.getReceptorResultados().setFinUsuario(false);
                                String resComprobar = ReceptorResultados.getReceptorResultados().obtenerResultadoUsuario();
                                if (resComprobar.equals("usuarioInexistente")){
                                    usuarioLogin.setError(getString(R.string.no_existe_usuario));
                                } else if (resComprobar.equals("contraIncorrecta")){
                                    contraLogin.setError(getString(R.string.contrasena_incorrecta));
                                }else{
                                    jugar(usuarioLogin.getText().toString());
                                }
                            }else {
                                handler.postDelayed(this, 200);
                            }
                        }
                    };
                    handler.postDelayed(run,100);
                }
            }
        });

        ImageView foto = registrar.getActivity().findViewById(R.id.fotoPerfil);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogoFoto= new DialogoImagenPerfil(LoginActivity.this);
                dialogoFoto.show();
            }
        });

        //Se asigna el listener al botón para que compruebe si los datos de registro son correctos, y si lo son, se va al juego. Si no, se muestra el error
        Button botonRegistrar = registrar.getActivity().findViewById(R.id.botonRegistrar);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText usuarioRegistrar = registrar.getActivity().findViewById(R.id.textRegistrarUsuario);
                EditText contraRegistrar = registrar.getActivity().findViewById(R.id.textRegistrarContra);
                EditText contraValidarRegistrar = registrar.getActivity().findViewById(R.id.textRegistrarValidContra);

                if (usuarioRegistrar.getText().toString().equals("")){
                    usuarioRegistrar.setError(getString(R.string.rellena_campo));
                }
                if (contraRegistrar.getText().toString().equals("")){
                    contraRegistrar.setError(getString(R.string.rellena_campo));
                }
                if (contraValidarRegistrar.getText().toString().equals("")){
                    contraValidarRegistrar.setError(getString(R.string.rellena_campo));
                }else if (!contraRegistrar.getText().toString().equals(contraValidarRegistrar.getText().toString())){
                    contraValidarRegistrar.setError(getString(R.string.contrasenas_no_coinciden));
                }else{
                    //Boolean estado = registrarUsuario(usuarioRegistrar.getText().toString(),contraRegistrar.getText().toString());
                    ComprobarExisteUsuario existe = new ComprobarExisteUsuario(usuarioRegistrar.getText().toString());
                    new Thread(existe).start();

                    Handler handler = new Handler();
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            if (ReceptorResultados.getReceptorResultados().haAcabadoExiste()){
                                String resExiste = ReceptorResultados.getReceptorResultados().obtenerResultadoExiste();
                                if (resExiste.equals("si")){
                                    usuarioRegistrar.setError(getString(R.string.existe_usuario));
                                }else{
                                    BitmapDrawable imagen = (BitmapDrawable) foto.getDrawable();
                                    Bitmap bitmap = imagen.getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                    byte[] fototransformada = stream.toByteArray();
                                    String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);

                                    //escribirAFichero();
                                    CrearUsuario crear = new CrearUsuario(usuarioRegistrar.getText().toString(), encriptar(contraRegistrar.getText().toString()),fotoen64);
                                    new Thread(crear).start();
                                    Runnable run2 = new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ReceptorResultados.getReceptorResultados().haAcabadoCrear()) {
                                                if (ReceptorResultados.getReceptorResultados().obtenerResultadoCrear().equals("ok")){
                                                    Utils.getUtils().enviarFCM(LoginActivity.this, "okCrear");
                                                    jugar(usuarioRegistrar.getText().toString());
                                                }else{
                                                    Utils.getUtils().enviarFCM(LoginActivity.this, "errorCrear");
                                                }
                                            }
                                            else{
                                                handler.postDelayed(this, 200);
                                            }
                                        }
                                    };
                                    handler.postDelayed(run2, 100);

                                }
                            }
                            else{
                                handler.postDelayed(this, 200);
                            }
                        }
                    };
                    handler.postDelayed(run, 100);
                    ReceptorResultados.getReceptorResultados().setFinExiste(false);


                }

            }
        });


    }

    /**
     * Se cambia el texto del botón para cambiar entre login y registro
     */
    public void cambiarBotonCambio(){
        if (getFragmentManager().findFragmentById(R.id.fragmentLogin).isHidden()) {
            ((TextView)findViewById(R.id.cambioLoginRegistrar)).setText(getString(R.string.no_tienes_usuario));
        } else {
            ((TextView)findViewById(R.id.cambioLoginRegistrar)).setText(getString(R.string.volver_login));
        }
    }

    /**
     * Se carga el json del fichero de usuarios y contraseñas a un HashMap, y si no existe, se crea un HashMap vacío
     */
    //https://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap/27679348
    private void leerDeFichero(){
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuCont.txt")));
            String datos = ficherointerno.readLine();
            Map<String,String> mapRes = new Gson().fromJson(datos, new TypeToken<HashMap<String, String>>() {}.getType());
            if (mapRes == null){
                mapRes = new HashMap<String, String>();
            }
            mapa = mapRes;
            ficherointerno.close();
        }catch (NullPointerException e){
            mapa = new HashMap<String, String>();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (mapa == null){
                mapa = new HashMap<String, String>();
            }
        }
    }

    /**
     * Se guarda el HashMap en formato json en el fichero de contraseñas, sobreescribiendo el que ya estaba
     */
    private void escribirAFichero(){
        try {
            String json = new Gson().toJson(new HashMap(mapa));
            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuCont.txt", Context.MODE_PRIVATE));
            fichero.write(json);
            fichero.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Se comprueba si los datos de login son correctos
     * @param usuario Nombre de usuario
     * @param contra Contraseña
     * @return Booleano que indica si es correcto
     */
    /*private Boolean comprobarLogin(String usuario, String contra){
        Boolean login = null;
        String contraEnc = encriptar(contra);
        if(contraEnc != null){
            if (mapa.containsKey(usuario)){
                if(mapa.get(usuario).equals(contraEnc)){
                    login = true;
                }
            } else {
                login = false;
            }
        }
        return login;
    }*/

    /**
     * Se coloca el usuario y la contraseña encriptada en el HashMap si no hay problemas con los datos
     * @param usuario Nombre de usuario
     * @param contra Contraseña
     * @return Booleano que representa si ha ido bien
     */
    /*private Boolean registrarUsuario(String usuario, String contra){
        String contraEnc = encriptar(contra);
        Boolean estado = null;
        if (mapa.containsKey(usuario)){
            estado = false;
        }else if (contraEnc != null){
            mapa.put(usuario,contraEnc);
            estado = true;
        }
        return estado;
    }*/

    /**
     * Encripta un string mediante el algoritmo Blowfish
     * @param string El string a encriptar
     * @return El string encriptado
     */
    //http://www.adeveloperdiary.com/java/how-to-easily-encrypt-and-decrypt-text-in-java/
    private String encriptar(String string){
        String encr = null;
        try {
            SecretKeySpec skeyspec=new SecretKeySpec("DAS".getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(string.getBytes());
            encr = new String(encrypted);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encr;
    }

    /**
     * Se guarda en el fichero de usuario logeado el usuario introducido y se va a la actividad principal
     * @param usu El nombre de usuario del jugador
     */
    private void jugar(String usu){
        try {
            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
            fichero.write(usu);
            fichero.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("usu", usu);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64 && resultCode == RESULT_OK) {

            Uri imagenSeleccionada = data.getData();
            Bitmap laminiatura = null;
            try {
                laminiatura = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenSeleccionada);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File eldirectorio = this.getFilesDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String nombrefichero = "IMG_" + timeStamp + "_";
            File imagenFich = new File(eldirectorio, nombrefichero + ".jpg");
            OutputStream os;
            try {
                os = new FileOutputStream(imagenFich);
                laminiatura.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                laminiatura = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagenSeleccionada);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap miniaturaCrop;
            if(laminiatura.getHeight() >= laminiatura.getWidth()){
                miniaturaCrop = Bitmap.createBitmap(laminiatura, 0, (laminiatura.getHeight()-laminiatura.getWidth())/2, laminiatura.getWidth(), laminiatura.getWidth());
            }else{
                miniaturaCrop = Bitmap.createBitmap(laminiatura, (laminiatura.getWidth()-laminiatura.getHeight())/2, 0, laminiatura.getHeight(), laminiatura.getHeight());
            }

            ((ImageView)getFragmentManager().findFragmentById(R.id.fragmentRegistrar).getActivity().findViewById(R.id.fotoPerfil)).setImageBitmap(miniaturaCrop);
        }
        if (requestCode == 70 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap laminiatura = (Bitmap) extras.get("data");
            Bitmap miniaturaCrop;
            if(laminiatura.getHeight() >= laminiatura.getWidth()){
                 miniaturaCrop = Bitmap.createBitmap(laminiatura, 0, (laminiatura.getHeight()-laminiatura.getWidth())/2, laminiatura.getWidth(), laminiatura.getWidth());
            }else{
                miniaturaCrop = Bitmap.createBitmap(laminiatura, (laminiatura.getWidth()-laminiatura.getHeight())/2, 0, laminiatura.getHeight(), laminiatura.getHeight());
            }
            ((ImageView)getFragmentManager().findFragmentById(R.id.fragmentRegistrar).getActivity().findViewById(R.id.fotoPerfil)).setImageBitmap(miniaturaCrop);
        }

    }

}