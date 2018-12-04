package com.example.alberto.concesionario.BaseDeDatos;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenhelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "ConcesionarioDatabase";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenhelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
