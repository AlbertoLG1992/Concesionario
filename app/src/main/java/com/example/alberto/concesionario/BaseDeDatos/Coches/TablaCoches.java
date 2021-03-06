package com.example.alberto.concesionario.BaseDeDatos.Coches;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.alberto.concesionario.BaseDeDatos.DatabaseOpenhelper;
import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    /**
     * Añade a la base de datos un coche
     *
     * @param coche :Coche
     */
    public void addCoche(Coche coche){
        /* Se abre la base de datos */
        this.openDatabaseWrite();
        /* Se comprime la imagen para guardarla en tipo blob */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        coche.getFoto().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        /* Se insertan en un ContentValues todos los campos y se hace un
         * Insert en la base de datos */
        if (this.database != null){
            ContentValues values = new ContentValues();
            values.put("marca", coche.getMarca());
            values.put("modelo", coche.getModelo());
            values.put("precio", String.valueOf(coche.getPrecio()));
            values.put("descripcion", coche.getDescripcion());
            values.put("foto", byteArrayOutputStream.toByteArray());
            values.put("es_nuevo", coche.getEsNuevo()? 1 : 0);
            this.database.insert("coches", null, values);
        }
        /* Se cierra la conexión */
        this.closeDatabase();
    }

    /**
     * Añade en la base de datos una serie de extras a un coche
     *
     * @param coche :Coche
     * @param listaExtras :ArrayList<Extra>
     */
    public void addExtrasDeCoche(Coche coche, ArrayList<Extra> listaExtras){
        /* Se abre la base de datos */
        this.openDatabaseWrite();

        /* Se recorre el arrayList para acer tantos insert como longitud tenga */
        if (this.database != null){
            for (int i = 0; i < listaExtras.size(); i++){
                ContentValues values = new ContentValues();
                values.put("id_coche", coche.getIdCoche());
                values.put("id_extras", listaExtras.get(i).getId());
                this.database.insert("coche_extra", null, values);
            }
        }
        /* Se cierra la base de datos */
        this.closeDatabase();
    }

    /**
     * Devuleve un coche en concreto respecto a su id, si el id no existe, devuelve un coche
     * vacío
     *
     * @param id :int
     * @return Coche
     */
    public Coche extraerCoche(int id){
        /* Se inicializa el coche para que en caso de que la consulta de un valor vacío
         * devuelva un coche vacío en lugar de que de error */
        Coche coche = new Coche();
        Cursor c;
        this.openDatabaseRead();
        c = this.database.rawQuery("SELECT * FROM coches WHERE id_coche = " + id, null);
        if (c.moveToFirst()){
            coche = new Coche(c.getInt(0), c.getString(1),
                    c.getString(2), c.getInt(3), c.getString(4),
                    BitmapFactory.decodeStream(new ByteArrayInputStream(c.getBlob(5))),
                    c.getInt(6) == 1?  true :  false);
        }
        this.closeDatabase();
        return coche;
    }

    /**
     * Método para extraer de la base de datos un coche sin conocer su id
     *
     * @param coche :Coche
     * @return Coche
     */
    public Coche extraerCocheSinId(Coche coche){
        Coche cocheOut = new Coche();
        Cursor c;
        this.openDatabaseRead();
        /* Como no se conoce el id se hace una busqueda por todos los campos
         * exceptuando el id */
        c = this.database.rawQuery("SELECT * FROM coches " +
                "WHERE marca = '" + coche.getMarca() + "' AND " +
                        "modelo = '" + coche.getModelo() + "' AND " +
                        "precio = " + coche.getPrecio() + " AND " +
                        "descripcion = '" + coche.getDescripcion() + "'",
                null);
        if (c.moveToFirst()){
            coche = new Coche(c.getInt(0), c.getString(1),
                    c.getString(2), c.getInt(3), c.getString(4),
                    BitmapFactory.decodeStream(new ByteArrayInputStream(c.getBlob(5))),
                    c.getInt(6) == 1?  true :  false);
        }
        this.closeDatabase();
        return coche;
    }

    /**
     * Se modifica un coche de la base de datos, se toman como parametros para el cambio
     * los datos del coche que entra por parametros
     *
     * @param coche :Coche
     */
    public void modificarCoche(Coche coche){
        this.openDatabaseWrite();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        coche.getFoto().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        if (this.database != null){
            ContentValues values = new ContentValues();
            values.put("marca", coche.getMarca());
            values.put("modelo", coche.getModelo());
            values.put("precio", String.valueOf(coche.getPrecio()));
            values.put("descripcion", coche.getDescripcion());
            values.put("foto", byteArrayOutputStream.toByteArray());
            values.put("es_nuevo", coche.getEsNuevo()? 1 : 0);
            this.database.update("coches", values, "id_coche = " + coche.getIdCoche(), null);
        }
        this.closeDatabase();
    }

    /**
     * Elimina un coche de la base de datos
     *
     * @param coche :Coche
     */
    public void eliminarCoche(Coche coche){
        this.openDatabaseWrite();
        this.database.execSQL("DELETE FROM coches WHERE id_coche = " + coche.getIdCoche());
        this.closeDatabase();
    }
}
