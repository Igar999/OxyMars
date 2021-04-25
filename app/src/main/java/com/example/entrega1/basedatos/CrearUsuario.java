package com.example.entrega1.basedatos;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CrearUsuario implements Runnable {

    private String usuario;
    private String contra;
    private String foto;


    public CrearUsuario(String pUsuario, String pContra, String pFoto){
        this.usuario = pUsuario;
        this.contra = pContra;
        this.foto = pFoto;
    }

    /**
     * Se crea y lanza la petición al servidor para crear un usuario en la base de datos del servidor con los datos introducidos como parámetros en la constructora
     * El resultado (éxito o fracaso) se almacena en ReceptorResultados
     */
    @Override
    public void run(){
        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", usuario)
                    .appendQueryParameter("contra", contra)
                    .appendQueryParameter("foto", foto);
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
            BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line, result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            inputStream.close();
            ReceptorResultados.getReceptorResultados().setFinCrear(true);
            if (result == ""){
                ReceptorResultados.getReceptorResultados().setResCrear("ok");
            }else {
                ReceptorResultados.getReceptorResultados().setResCrear("fail");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
