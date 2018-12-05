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

    /**
     * Devuelve un arrayList con todos los coches nuevos o usados de la base de datos,
     * los nuevos con true y los usados con false
     *
     * @param esNuevo :boolean
     * @return ArrayList<Coche>
     */
    public ArrayList<Coche> todosLosCoches(boolean esNuevo){
        ArrayList<Coche> listaCoches = new ArrayList<Coche>();
        Cursor c;
        int nuevoUsado = esNuevo? 1 : 0;

        /* Se abre la base de datos y se extrae en el cursor todos los coches existentes */
        this.openDatabaseRead();
        c = this.database.rawQuery("SELECT * FROM coches WHERE es_nuevo = " + nuevoUsado, null);

        /* Se compueba que la consulta no esta vacía */
        if (c.moveToFirst()){
            do {
                /* Para extraer un blob es necesario pasarlo a un array de byte y después crear un
                 * ByteArrayInputStream en el cual lo metemos mediante el constructor para despues
                 * pasarlo a Bitmap mediante BitmapFactory.decodeStream()
                    * byte[] blob = c.getBlob(5);
                    * ByteArrayInputStream byteIn = new ByteArrayInputStream(blob);
                    * Bitmap foto = BitmapFactory.decodeStream(byteIn);
                 * o de manera abreviada:
                    * Bitmap  foto = BitmapFactory.decodeStream(new ByteArrayInputStream(c.getBlob(5)));
                 *
                 * Dado que SQLite no posee boolean, se extrae un entero y se guarda true o false
                 * mediante un operador ternario:
                    * boolean esNuevoBool = c.getInt(6) == 1?  true :  false; */
                listaCoches.add(new Coche(c.getInt(0), c.getString(1),
                        c.getString(2), c.getInt(3), c.getString(4),
                        BitmapFactory.decodeStream(new ByteArrayInputStream(c.getBlob(5))),
                        c.getInt(6) == 1?  true :  false));
            }while (c.moveToNext());
        }

        c.close();
        this.closeDatabase();

        return listaCoches;
    }

}
