package com.example.entrega1.basedatos;

import android.net.Uri;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ObtenerUsuariosRanking implements Runnable {

    private String usuario;

    private Boolean enEjecucion = false;

    /**
     * Se crea y lanza la petición para obtener todos los usuarios junto con sus cantidades de Oxígeno, para rellenar el ranking
     * El resultado (los datos) se almacena en la clase ReceptorResultado
     */
    @Override
    public void run(){
        if (!ReceptorResultados.getReceptorResultados().haAcabadoRanking() && !enEjecucion){
            try {
                enEjecucion = true;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("foo", "bar");
                String parametros = builder.build().getEncodedQuery();

                String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/obtenerUsuariosRanking.php";
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
                if (statusCode == 200) {
                    BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line, result = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    inputStream.close();
                    ReceptorResultados.getReceptorResultados().setResRanking(new JSONArray(result));
                    enEjecucion = false;
                    ReceptorResultados.getReceptorResultados().setFinRanking(true);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

}
