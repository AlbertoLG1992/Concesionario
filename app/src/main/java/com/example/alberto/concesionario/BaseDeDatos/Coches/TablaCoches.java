package com.example.alberto.concesionario.BaseDeDatos.Coches;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.alberto.concesionario.BaseDeDatos.DatabaseOpenhelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TablaCoches {

    private SQLiteAssetHelper openHelper;
    private SQLiteDatabase database;
    private static TablaCoches instance;

    /**
     * Constructor de clase
     *
     * @param context: Context
     */
    public TablaCoches(Context context){
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

    /**  **/
    public ArrayList<Coche> todosLosCoches(){
        ArrayList<Coche> listaCoches = new ArrayList<Coche>();
        Cursor c;

        /* Se abre la base de datos y se extrae en el cursor todos los coches existentes */
        this.openDatabaseRead();
        c = this.database.rawQuery("SELECT * FROM coches", null);

        if (c.moveToFirst()){
            do {
                /* Se extraen todos los datos */
                int id = c.getInt(0);
                String marca = c.getString(1);
                String modelo = c.getString(2);
                int precio = c.getInt(3);
                String descrip = c.getString(4);
                //byte[] blob = c.getBlob(5);
                //ByteArrayInputStream byteIn = new ByteArrayInputStream(blob);
                //Bitmap foto = BitmapFactory.decodeStream(byteIn);
                Bitmap foto = BitmapFactory.decodeStream(new ByteArrayInputStream(c.getBlob(5)));
                int esNuevoInt = c.getInt(6);
                boolean esNuevoBool;

                if (esNuevoInt == 1){
                    esNuevoBool = true;
                }else {
                    esNuevoBool = false;
                }
                listaCoches.add(new Coche(id, marca, modelo, precio, descrip, foto, esNuevoBool));
            }while (c.moveToNext());
        }

        c.close();
        this.closeDatabase();

        return listaCoches;
    }

}
