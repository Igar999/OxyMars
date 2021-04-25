package com.example.entrega1.basedatos;

import android.net.Uri;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class BorrarUsuario implements Runnable {

    private String usuario;


    public BorrarUsuario(String pUsuario){
        this.usuario = pUsuario;
    }

    @Override
    public void run(){
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", usuario);
            String parametros = builder.build().getEncodedQuery();

            String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/borrarUsuario.php";
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
