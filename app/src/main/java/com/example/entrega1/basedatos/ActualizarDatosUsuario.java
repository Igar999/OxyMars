package com.example.entrega1.basedatos;

import android.net.Uri;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActualizarDatosUsuario implements Runnable {

    private String usuario;
    private float oxigeno;
    private float oxiToque;
    private float oxiSegundo;
    private int desbloqueadoToque;
    private int desbloqueadoSegundo;


    public ActualizarDatosUsuario(String pUsuario,  float pOxigeno, float pOxiToque, float pOxiSegundo, int pDesbloqueadoToque, int pDesbloqueadoSegundo){
        this.usuario = pUsuario;
        this.oxigeno = pOxigeno;
        this.oxiToque = pOxiToque;
        this.oxiSegundo = pOxiSegundo;
        this.desbloqueadoToque = pDesbloqueadoToque;
        this.desbloqueadoSegundo = pDesbloqueadoSegundo;
    }

    @Override
    public void run(){
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", usuario)
                    .appendQueryParameter("oxigeno", String.valueOf(oxigeno))
                    .appendQueryParameter("oxiToque", String.valueOf(oxiToque))
                    .appendQueryParameter("oxiSegundo", String.valueOf(oxiSegundo))
                    .appendQueryParameter("desbloqueadoToque", String.valueOf(desbloqueadoToque))
                    .appendQueryParameter("desbloqueadoSegundo", String.valueOf(desbloqueadoSegundo));
            String parametros = builder.build().getEncodedQuery();

            String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/actualizarDatosUsuario.php";
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
            ReceptorResultados.getReceptorResultados().setFinActualizar(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
