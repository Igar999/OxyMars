package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class AjustesActivity extends AppCompatActivity {

    String usuario = "";
    Oxigeno oxi = Oxigeno.getOxi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
            TextView textoUsuario = findViewById(R.id.textUsuarioLogout);
            textoUsuario.setText("Usuario: " + usuario);
        }

        Button logout = findViewById(R.id.botonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
                    fichero.write("");
                    fichero.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

                Intent i = new Intent(AjustesActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        ImageView twitter = findViewById(R.id.twitterImg);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTwitter("¡Genial! He conseguido " + oxi.ponerCantidad(oxi.getOxigeno()) + " de oxígeno para mi planeta en OxyMars.");
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AjustesActivity.this, MainActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }

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
            //Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }

}