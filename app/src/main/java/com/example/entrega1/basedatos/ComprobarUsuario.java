package com.example.entrega1.basedatos;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ComprobarUsuario implements Runnable {

    private String usuario;
    private String contra;

    private Boolean enEjecucion = false;


    public ComprobarUsuario(String pUsuario, String pContra){
        this.usuario = pUsuario;
        this.contra = pContra;
    }

    @Override
    public void run(){
        if (!ReceptorResultados.getReceptorResultados().haAcabadoUsuario() && !enEjecucion){
            try {
                enEjecucion = true;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("usuario", usuario)
                        .appendQueryParameter("contra", contra);
                String parametros = builder.build().getEncodedQuery();

                String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/igarcia353/WEB/comprobarUsuario.php";
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

                if (result.equals("usuarioInexistente") || result.equals("contraIncorrecta") || result.equals("correcto")){
                    ReceptorResultados.getReceptorResultados().setResUsuario(result);
                }
                enEjecucion = false;
                ReceptorResultados.getReceptorResultados().setFinUsuario(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
