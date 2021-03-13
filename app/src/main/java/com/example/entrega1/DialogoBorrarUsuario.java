package com.example.entrega1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.OutputStreamWriter;

public class DialogoBorrarUsuario extends Dialog {

    private Activity padre;
    public DialogoBorrarUsuario(@NonNull Context context) {
        super(context);
        this.padre = (Activity)context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_borrar);

        Button botonSi = findViewById(R.id.botonSiBorrar);
        Button botonNo = findViewById(R.id.botonNoBorrar);
        botonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                padre.finish();
                GuardarDatos GestorDB = new GuardarDatos (padre, "OxyMars", null, 1);
                SQLiteDatabase bd = GestorDB.getWritableDatabase();
                String[] argumentos = new String[] {Utils.getUtils().getUsuario()};
                bd.delete("Datos", "Usuario=?", argumentos);

                try {
                    OutputStreamWriter fichero = new OutputStreamWriter(padre.openFileOutput("usuLog.txt", Context.MODE_PRIVATE));
                    fichero.write("");
                    fichero.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
                Intent i = new Intent(padre, LoginActivity.class);
                i.putExtra("borrar", Utils.getUtils().getUsuario());
                padre.startActivity(i);
                padre.finish();
            }
        });

        botonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
