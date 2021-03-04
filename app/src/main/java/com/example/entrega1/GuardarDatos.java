package com.example.entrega1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GuardarDatos extends SQLiteOpenHelper {
    public GuardarDatos(@Nullable Context context, @Nullable String name,
                        @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


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
