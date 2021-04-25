package com.example.entrega1.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//-----------------------------------------------------------------------
//CLASE QUE SE UTILIZA PARA LA BASE DE DATOS LOCAL, ACTUALMENTE NO SE USA
//-----------------------------------------------------------------------
public class GuardarDatos extends SQLiteOpenHelper {
    /**
     * Constructora del gestor de la base de datos
     * @param context El contexto
     * @param name El nombre
     * @param factory El factory
     * @param version La versión
     */
    public GuardarDatos(@Nullable Context context, @Nullable String name,
                        @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Se crea la tabla para almacenar los datos si no existe
     * @param db La base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Datos (" +
                "'Usuario' VARCHAR(20) PRIMARY KEY NOT NULL, " +
                "'Oxigeno' FLOAT(255), " +
                "'OxiToque' FLOAT(255), " +
                "'OxiSegundo' FLOAT(255), " +
                "'DesbloqueadoToque' INT(10), " +
                "'DesbloqueadoSegundo' INT(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
