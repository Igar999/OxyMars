package com.example.entrega1.runnables;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ComprobarExisteUsuario implements Runnable {

    private String usuario;

    private Boolean enEjecucion = false;

    public ComprobarExisteUsuario(String pUsuario){
        this.usuario = pUsuario;
    }

    @Override
    public void run(){
        if (!ReceptorResultados.getReceptorResultados().haAcabadoExiste() && !enEjecucion){
            try {
                enEjecucion = true;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("usuario", usuario);
                String parametros = builder.build().getEncodedQuery();

                String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/comprobarExisteUsuario.php";
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
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

                if (result.equals("no") || result.equals("si")){
                    ReceptorResultados.getReceptorResultados().setResExiste(result);
                }
                enEjecucion = false;
                ReceptorResultados.getReceptorResultados().setFinExiste(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
