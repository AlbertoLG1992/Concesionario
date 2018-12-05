package com.example.alberto.concesionario.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Coches.TablaCoches;
import com.example.alberto.concesionario.R;

import java.util.ArrayList;

public class AdapterCoches extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Coche> items;

    /**
     * Constructor de clase, mediante un booleano se sabrá si se carga coches nuevos(true) o
     * coches usados(false)
     *
     * @param activity :Activity
     * @param context :Context
     * @param esNuevo :Boolean
     */
    public AdapterCoches(Activity activity, Context context, boolean esNuevo){
        /* En el mismo constructor se hace la consulta a la base de datos y
         * se carga en el ArrayList */
        TablaCoches tablaCoches = new TablaCoches(context);
        this.items = tablaCoches.todosLosCoches(esNuevo);
        this.activity = activity;
    }

    /**
     * Devuelve el total de los elementos cargados en el listView
     *
     * @return int
     */
    @Override
    public int getCount() {
        return this.items.size();
    }

    /**
     * Devuelve el item seleccionado
     *
     * @param position :int
     * @return Object
     */
    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    /**
     * Devuelve el id del item seleccionado
     *
     * @param position :int
     * @return long
     */
    @Override
    public long getItemId(int position) {
        return this.items.get(position).getIdCoche();
    }

    /**
     * Carga los datos del ArrayList en el ListView
     *
     * @param position :int
     * @param convertView :View
     * @param parent :ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Se carga el View, y en caso de que no se cargue bien se introduce manualmente
         * los parametros para que detecte el XML el cual tiene que cargar */
        View v = convertView;
        if (convertView == null){
            LayoutInflater infla = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = infla.inflate(R.layout.adapter_coches_nuevos, null);
        }

        /* Se extrae el item especifico que toca cargar y se añade a los objetos del XML */
        Coche coche = items.get(position);

        TextView txvMarcaCocheNuevoLista = (TextView)v.findViewById(R.id.txvMarcaCocheNuevoLista);
        txvMarcaCocheNuevoLista.setText(coche.getMarca());

        TextView txvModeloCocheNuevoLista = (TextView)v.findViewById(R.id.txvModeloCocheNuevoLista);
        txvModeloCocheNuevoLista.setText(coche.getModelo());

        ImageView imgCocheNuevoLista = (ImageView)v.findViewById(R.id.imgListCocheNuevoLista);
        imgCocheNuevoLista.setImageBitmap(coche.getFoto());

        return v;
    }
}
