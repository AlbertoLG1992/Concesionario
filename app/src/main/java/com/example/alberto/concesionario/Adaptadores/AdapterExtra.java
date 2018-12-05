package com.example.alberto.concesionario.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.BaseDeDatos.Extras.TablaExtras;
import com.example.alberto.concesionario.R;

import java.util.ArrayList;

public class AdapterExtra extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Extra> items;

    /**
     * Constructor de clase
     *
     * @param activity :Activity
     * @param context :Context
     */
    public AdapterExtra(Activity activity, Context context) {
        TablaExtras tablaExtras = new TablaExtras(context);
        this.items = tablaExtras.todosLosExtras();
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
        return this.items.get(position).getId();
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
            v = infla.inflate(R.layout.adapter_extras, null);
        }

        /* Se extrae el item especifico que toca cargar y se añade a los objetos del XML */
        Extra extra = items.get(position);

        TextView txvNombreExtraLista = (TextView)v.findViewById(R.id.txvNombreExtraLista);
        txvNombreExtraLista.setText(extra.getNombre());

        TextView txvDescripExtraLista = (TextView)v.findViewById(R.id.txvDescripExtraLista);
        txvDescripExtraLista.setText(extra.getDescripcion());

        TextView txvPrecioExtraLista = (TextView)v.findViewById(R.id.txvPrecioExtraLista);
        txvPrecioExtraLista.setText(String.valueOf(extra.getPrecio()) + "€");

        return v;
    }
}
