package com.example.entrega1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AyudaActivity extends AppCompatActivity {

    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usu");
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AyudaActivity.this, AjustesActivity.class);
        i.putExtra("usu", usuario);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.getUtils().musicaPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.getUtils().musicaPlay();
    }
}