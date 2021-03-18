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

    /**
     * Constructora del diálogo
     * @param context El contexto de la actividad donde se muestra el diálogo
     */
    public DialogoBorrarUsuario(@NonNull Context context) {
        super(context);
        this.padre = (Activity)context;
    }

    /**
     * Se crea el diálogo, asignando las acciones correspondientes a cada botón mediante listeners. Si dice que sí, se accede a la base de datos para borrar el usuario y a la pantalla de login se le pasa el nombre de usuario, para borrarlo del fichero de usuarios y contraseñas
     * @param savedInstanceState
     */
    //https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
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
