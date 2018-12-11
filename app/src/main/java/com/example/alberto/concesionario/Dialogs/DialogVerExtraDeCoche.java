package com.example.alberto.concesionario.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.alberto.concesionario.Adaptadores.AdapterExtra;
import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.BaseDeDatos.Extras.TablaExtras;
import com.example.alberto.concesionario.R;

import java.util.ArrayList;

public class DialogVerExtraDeCoche extends DialogFragment {

    Coche coche;
    ArrayList<Extra> listExtras;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /* Se crea un layoutInflater para poder cargar el xml en un View */
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ver_cohe_extra, null);

        ListView listView = (ListView)view.findViewById(R.id.listViewDialogExtra);

        /* Se crea el adaptador y se dice que se filtra por el coche */
        AdapterExtra adapterExtra = new AdapterExtra(getActivity(), view.getContext());
        adapterExtra.filtrarPorCoche(this.coche, getContext());
        listView.setAdapter(adapterExtra);

        /* Se le introduce el view y solo ponemos el setPositiveButton, pues solo
         * es a modo informativo, no va a devolver nada */
        builder.setView(view)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



        return builder.create();
    }

    public void setCoche(Coche coche){
        this.coche = coche;
    }
}
