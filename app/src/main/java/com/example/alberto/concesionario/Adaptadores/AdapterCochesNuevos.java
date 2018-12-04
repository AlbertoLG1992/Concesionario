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
import com.example.alberto.concesionario.R;

import java.util.ArrayList;

public class AdapterCochesNuevos extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Coche> items;

    /**
     * Constructor de clase
     *
     * @param activity :Activity
     * @param items :Arraylist<Coche>
     */
    public AdapterCochesNuevos(Activity activity, ArrayList<Coche> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.items.get(position).getIdCoche();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null){
            LayoutInflater infla = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = infla.inflate(R.layout.adapter_coches_nuevos, null);
        }

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
