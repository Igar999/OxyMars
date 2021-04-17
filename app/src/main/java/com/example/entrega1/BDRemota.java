package com.example.entrega1;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BDRemota {

    private Boolean finHilo = false;
    private String error = null;

    public void resetearFlag(){
        this.finHilo = false;
    }

    public Boolean hiloAcabado(){
        return finHilo;
    }

    public void resetearError(){
        this.error = null;
    }

    public String errorLogin(){
        return this.error;
    }

    public Runnable crearUsuario(String usuario, String contra){
        return new Runnable() {
            @Override
            public void run() {
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
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public Runnable actualizarDatosUsuario(String usuario,  float oxigeno, float oxiToque, float oxiSegundo, int desbloqueadoToque, int desbloqueadoSegundo){
        return new Runnable() {
            @Override
            public void run() {
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
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }



    public Runnable borrarUsuario(String usuario){
        return new Runnable() {
            @Override
            public void run() {
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
        };
    }


    public Runnable obtenerDatosUsuario(String usuario){
        return new Runnable() {
            @Override
            public void run() {
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
                        Oxigeno.getOxi().actualizarValores(
                                jsonObject.getLong("oxigeno"),
                                jsonObject.getLong("oxiToque"),
                                jsonObject.getLong("oxiSegundo"),
                                jsonObject.getInt("desbloqueadoToque"),
                                jsonObject.getInt("desbloqueadoSegundo")
                        );
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public Runnable comprobarUsuario(String usuario, String contra){
        return new Runnable() {
            @Override
            public void run() {
                try {
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

                    if (result == "usuarioInexistente" || result == "contraIncorrecta"){
                        error = result;
                    }
                    finHilo = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public Runnable comprobarExisteUsuario(String usuario){
        return new Runnable() {
            @Override
            public void run() {
                try {
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

                    if (result == "0"){

                    }
                    finHilo = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    public Runnable prueba(String usuario){
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("usuario", usuario);
                    String parametros = builder.build().getEncodedQuery();

                    String direccion = "http://ec2-54-167-31-169.compute-1.amazonaws.com/Xigarcia353/WEB/obtenerDatosDeUsuario.php";
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
                        ArrayList<String> lista = new ArrayList<>();
                        /*for(int i = 0; i < jsonArray.length(); i++)
                        {
                            String nombre = jsonArray.getJSONObject(i).getString("Nombre");
                            lista.add(nombre);
                        }*/

                        float oxigeno = jsonObject.getLong("oxigeno");
                        float oxiToque = jsonObject.getLong("oxiToque");
                        float oxiSegundo = jsonObject.getLong("oxiSegundo");
                        int desbloqueadoToque = jsonObject.getInt("desbloqueadoToque");
                        int desbloqueadoSegundo = jsonObject.getInt("desbloqueadoSegundo");


                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
    }


}
