package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class LoginActivity extends AppCompatActivity {

    private Map<String,String> mapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String usuario = comprobarUsuarioLogeado();
        if (usuario != ""){
            jugar(usuario);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils utils = Utils.getUtils();

        leerDeFichero();

        Fragment login =  getFragmentManager().findFragmentById(R.id.fragmentLogin);
        Fragment registrar = getFragmentManager().findFragmentById(R.id.fragmentRegistrar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("borrar") != null){
                mapa.remove(extras.getString("borrar"));
                escribirAFichero();
            }
        }

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            utils.hideFragment(registrar,this);
            utils.showFragment(login,this);
        }else{
            utils.showFragment(registrar,this);
            utils.showFragment(login,this);
        }

        TextView cambio = findViewById(R.id.cambioLoginRegistrar);
        cambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.showHideFragment(login,LoginActivity.this);
                utils.showHideFragment(registrar,LoginActivity.this);
                cambiarBotonCambio();
            }
        });

        Button botonLogin = login.getActivity().findViewById(R.id.botonLogin);


        Button botonRegistrar = login.getActivity().findViewById(R.id.botonRegistrar);

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuarioLogin = login.getActivity().findViewById(R.id.textLoginUsuario);
                EditText contraLogin = login.getActivity().findViewById(R.id.textLoginContra);
                Boolean estado = comprobarLogin(usuarioLogin.getText().toString(),contraLogin.getText().toString());

                if (contraLogin.getText().toString().equals("")){
                    contraLogin.setError(getString(R.string.rellena_campo));
                }
                if (usuarioLogin.getText().toString().equals("")){
                    usuarioLogin.setError(getString(R.string.rellena_campo));
                }else if (estado == null){
                    contraLogin.setError(getString(R.string.contrasena_incorrecta));
                }else if (estado == false){
                    usuarioLogin.setError(getString(R.string.no_existe_usuario));
                }else{
                    jugar(usuarioLogin.getText().toString());
                }
            }
        });

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText usuarioRegistrar = login.getActivity().findViewById(R.id.textRegistrarUsuario);
                EditText contraRegistrar = login.getActivity().findViewById(R.id.textRegistrarContra);
                EditText contraValidarRegistrar = login.getActivity().findViewById(R.id.textRegistrarValidContra);

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
                    Boolean estado = registrarUsuario(usuarioRegistrar.getText().toString(),contraRegistrar.getText().toString());
                    if (estado == null){
                        contraRegistrar.setError(getString(R.string.contrasena_no_admitida));
                    }else if (estado == false){
                        usuarioRegistrar.setError(getString(R.string.existe_usuario));
                    }else{
                        escribirAFichero();
                        jugar(usuarioRegistrar.getText().toString());
                    }
                }

            }
        });


    }

    private String comprobarUsuarioLogeado() {
        String usuarioLog = "";
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuLog.txt")));
            usuarioLog = ficherointerno.readLine();
            if (usuarioLog == null){
                usuarioLog = "";
            }
            ficherointerno.close();
        } catch (Exception e){
            e.printStackTrace();
            try{
                OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
                fichero.write("");
                fichero.close();
                usuarioLog = "";
            } catch (Exception f){
                f.printStackTrace();
            }
        }
        return usuarioLog;
    }

    public void cambiarBotonCambio(){
        if (getFragmentManager().findFragmentById(R.id.fragmentLogin).isHidden()) {
            ((TextView)findViewById(R.id.cambioLoginRegistrar)).setText(getString(R.string.no_tienes_usuario));
        } else {
            ((TextView)findViewById(R.id.cambioLoginRegistrar)).setText(getString(R.string.volver_login));
        }
    }

    private void leerDeFichero(){
        try {
            BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(openFileInput("usuCont.txt")));
            String datos = ficherointerno.readLine();
            Map<String,String> mapRes = new Gson().fromJson(datos, new TypeToken<HashMap<String, String>>() {}.getType()); //https://stackoverflow.com/questions/21720759/convert-a-json-string-to-a-hashmap/27679348
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

    private Boolean comprobarLogin(String usuario, String contra){
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
    }

    private Boolean registrarUsuario(String usuario, String contra){
        String contraEnc = encriptar(contra);
        Boolean estado = null;
        if (mapa.containsKey(usuario)){
            estado = false;
        }else if (contraEnc != null){
            mapa.put(usuario,contraEnc);
            estado = true;
        }
        return estado;
    }

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



}