package com.example.entrega1.runnables;

import android.net.Uri;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CrearUsuario implements Runnable {

    private String usuario;
    private String contra;


    public CrearUsuario(String pUsuario, String pContra){
        this.usuario = pUsuario;
        this.contra = pContra;
    }

    @Override
    public void run(){
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", usuario)
                    .appendQueryParameter("contra", contra);
            String parametros = builder.build().getEncodedQuery();

            String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/crearUsuario.php";
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
            ReceptorResultados.getReceptorResultados().setFinCrear(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
