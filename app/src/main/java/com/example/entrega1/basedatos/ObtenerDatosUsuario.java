package com.example.entrega1.basedatos;

import android.net.Uri;

import com.example.entrega1.Oxigeno;
import com.example.entrega1.Utils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ObtenerDatosUsuario implements Runnable {

    private String usuario;


    public ObtenerDatosUsuario(String pUsuario){
        this.usuario = pUsuario;
    }

    /**
     * Crea y lanza la petición para obtener los datos del usuario introducido como parámetro en la constructora
     * El resultado (los datos) se guarda en la clase Oxígeno
     */
    @Override
    public void run(){
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", usuario);
            String parametros = builder.build().getEncodedQuery();

            String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/obtenerDatosUsuario.php";
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
                JSONObject jsonObject = new JSONObject(result);
                Utils.getUtils().imagenUsuario(jsonObject.getString("foto"));
                Oxigeno.getOxi().actualizarValores(
                        jsonObject.getLong("oxigeno"),
                        jsonObject.getLong("oxiToque"),
                        jsonObject.getLong("oxiSegundo"),
                        jsonObject.getInt("desbloqueadoToque"),
                        jsonObject.getInt("desbloqueadoSegundo")
                );
                ReceptorResultados.getReceptorResultados().setFinCargar(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
