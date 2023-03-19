package com.example.proyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {

    private static final String name = "bd";
    private static final int version = 1;


    public BaseDeDatos(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se crean las tablas de la base de datos
        db.execSQL("CREATE TABLE datosUsuarios ('Usuario' VARCHAR PRIMARY KEY, 'Correo' VARCHAR, 'Contraseña' VARCHAR)");
        db.execSQL("CREATE TABLE publicaciones ('Id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'Imagen' INT, 'Usuario' VARCHAR, 'Texto' VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}