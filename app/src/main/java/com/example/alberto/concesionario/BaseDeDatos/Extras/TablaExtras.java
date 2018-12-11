package com.example.alberto.concesionario.BaseDeDatos.Extras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
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

    /**
     * Se añade un extra en la base de datos
     *
     * @param extra :Extra
     */
    public void addExtra(Extra extra){
        this.openDatabaseWrite();
        if (this.database != null){
            ContentValues values = new ContentValues();
            values.put("nombre", extra.getNombre());
            values.put("descripcion", extra.getDescripcion());
            values.put("precio", extra.getPrecio());
            this.database.insert("extras", null, values);
        }
        this.closeDatabase();
    }


    /**
     * Devuelve un arrayList con los extras de un coche
     *
     * @param coche :Coche
     * @return :ArrayList<Extra>
     */
    public ArrayList<Extra> verExtrasDeCoche(Coche coche){
        ArrayList<Extra> listaExtras = new ArrayList<Extra>();
        Cursor c;

        this.openDatabaseRead();
        c = this.database.rawQuery(
                "SELECT extras.id_extras, extras.nombre, extras.descripcion, extras.precio " +
                "FROM coche_extra " +
                    "INNER JOIN extras on coche_extra.id_extras = extras.id_extras " +
                "WHERE coche_extra.id_coche = " + coche.getIdCoche(), null);

        if (c.moveToFirst()){
            do{
                listaExtras.add(new Extra(c.getInt(0), c.getString(1),
                        c.getString(2), c.getInt(3)));
            }while (c.moveToNext());
        }

        c.close();
        this.closeDatabase();

        return listaExtras;
    }

    /**
     * Método que pregunta si existen dependencias de un extra con los coches usados
     * para poder borrarlo
     *
     * @param extra :Extra
     * @return boolean true si existe y false en caso contrario
     */
    public boolean existenDependencias(Extra extra){
        boolean existe;
        Cursor c;

        this.openDatabaseRead();
        c = this.database.rawQuery("SELECT * FROM coche_extra WHERE id_extras = " + extra.getId(), null);

        existe = c.moveToFirst()? true: false;

        c.close();
        this.closeDatabase();

        return existe;
    }

    /**
     * Elimina un extra de la base de datos
     *
     * @param extra :Extra
     */
    public void eliminarExtra(Extra extra){
        this.openDatabaseWrite();
        this.database.execSQL("DELETE FROM extras WHERE id_extras = " + extra.getId());
        this.closeDatabase();
    }
}
