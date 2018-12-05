package com.example.alberto.concesionario.BaseDeDatos.Extras;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alberto.concesionario.BaseDeDatos.Coches.TablaCoches;
import com.example.alberto.concesionario.BaseDeDatos.DatabaseOpenhelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class TablaExtras {

    private SQLiteAssetHelper openHelper;
    private SQLiteDatabase database;
    private static TablaCoches instance;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public TablaExtras(Context context){
        this.openHelper = new DatabaseOpenhelper(context);
    }

    /**
     * Abre la conexión la base de datos en modo lectura
     */
    private void openDatabaseRead(){
        this.database = this.openHelper.getReadableDatabase();
    }

    /**
     * Abre la conexión a la base de datos en modo escritura
     */
    private void openDatabaseWrite(){
        this.database = this.openHelper.getWritableDatabase();
    }

    /**
     * Cierra la conexión a la base de datos
     */
    private void closeDatabase(){
        if (this.database != null){
            this.database.close();
        }
    }

    /**
     * Devuelve un ArrayList con todos los extras
     *
     * @return ArrayList<Extra>
     */
    public ArrayList<Extra> todosLosExtras(){
        ArrayList<Extra> listaExtras = new ArrayList<Extra>();
        Cursor c;

        /* Se abre la base de datos y se extrae en el cursor todos los coches existentes */
        this.openDatabaseRead();
        c = this.database.rawQuery("SELECT * FROM extras", null);

        /* Se compueba que la consulta no esta vacía */
        if (c.moveToFirst()){
            do {
                /* Se extraen los datos y se agregan a la lista de extras */
                listaExtras.add(new Extra(c.getInt(0), c.getString(1),
                        c.getString(2), c.getInt(3)));
            }while (c.moveToNext());
        }

        c.close();
        this.closeDatabase();

        return listaExtras;
    }
}
