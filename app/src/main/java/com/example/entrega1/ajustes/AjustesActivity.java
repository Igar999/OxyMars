package com.example.entrega1.ajustes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entrega1.Actividad;
import com.example.entrega1.juego.MainActivity;
import com.example.entrega1.Oxigeno;
import com.example.entrega1.R;
import com.example.entrega1.Utils;
import com.example.entrega1.loginregistro.LoginActivity;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class AjustesActivity extends Actividad {

    String usuario = "";
    Oxigeno oxi = Oxigeno.getOxi();

    /**
     * Se explica en el código con comentarios
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        //Se obtiene el usuario actual para ponerlo en el lugar en el que se muestra el usuario actual
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
            TextView textoUsuario = findViewById(R.id.textUsuarioLogout);
            textoUsuario.setText(usuario);
        }

        //Se establece el listener del botón de cierre de sesión para que al pulsarlo cierre sesión
        Button logout = findViewById(R.id.botonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
                    fichero.write("");
                    fichero.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(AjustesActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        //Se establece el listener para que  si se mantiene pulsado el botón, muestre el diálogo para confirmar que se quiere borrar el usuario
        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Dialog dialogoborrar = new DialogoBorrarUsuario(AjustesActivity.this);
                dialogoborrar.show();
                return false;
            }
        });

        //Se establece el listener para que el botón lleve a Twitter con el texto correspondiente
        ImageView twitter = findViewById(R.id.twitterImg);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTwitter(getString(R.string.twit1) + oxi.ponerCantidad(oxi.getOxigeno(), true) + getString(R.string.twit2));
            }
        });

        //Se establece el listener para que el botón lleve a la actividad que muestra el texto de ayuda
        ImageView info = findViewById(R.id.infoImg);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AjustesActivity.this, AyudaActivity.class);
                i.putExtra("usu", usuario);
                startActivity(i);
                finish();
            }
        });

        //Se establece la imagen de usuario, que está almacenada en Utils
        ImageView imagenUsuario = findViewById(R.id.imagenUsuario);
        byte[] bytes = Base64.decode(Utils.getUtils().getImagenUsu(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imagenUsuario.setImageBitmap(bitmap);
    }

    /**
     * Al pulsar el botón "atrás", se reproduce un sonido y se vuelve a la actividad principal
     */
    @Override
    public void onBackPressed() {
        Utils.getUtils().reproducirSonido(this, R.raw.atras);
        Intent i = new Intent(AjustesActivity.this, MainActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }

    /**
     * Se va a Twitter (app o web) para poder publicar la cantidad de oxígeno actual del jugador (es necesario tener sesión iniciada en Twitter)
     * @param message El mensaje a escribir
     */
    //https://stackoverflow.com/questions/14317512/how-can-i-post-on-twitter-with-intent-action-send
    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
        }
    }

    /**
     * Se codifica la url pasada como parámetro
     * @param s La url a codificar
     * @return La url codificada
     */
    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }
}