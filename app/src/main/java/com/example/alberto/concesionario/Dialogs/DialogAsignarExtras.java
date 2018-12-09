package com.example.alberto.concesionario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.BaseDeDatos.Extras.TablaExtras;

import java.util.ArrayList;

public class DialogAsignarExtras extends DialogFragment {
    private String[] nombreExtras;
    private boolean[] chekedExtras;
    private ArrayList<Extra> listaExtras;
    private ArrayList<Extra> listaExtrasOut;

    respuestaDialogAsignarExtras respuesta;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        /* Ya que para crear un dialog MultiChoiceItem es necesario insertar un vector de String
         * y otro de boolean, se hace una consulta de todos los extras y se recorre para crear
         * los vectores */
        TablaExtras tablaExtras = new TablaExtras(getContext());
        listaExtras = tablaExtras.todosLosExtras();
        nombreExtras = new String[listaExtras.size()];
        chekedExtras = new boolean[listaExtras.size()];
        for (int i = 0; i < listaExtras.size(); i++){
            nombreExtras[i] = listaExtras.get(i).getNombre();
            chekedExtras[i] = false;
        }

        /* Se crea el Builder con la opción de MultichoiceItems */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Indica los extras del coche");
        builder.setMultiChoiceItems(nombreExtras, chekedExtras, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                /* Con el vector de bollean se podrá saber cuales han sido marcados */
                chekedExtras[which] = isChecked;
            }
        });

        /* Botón si */
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listaExtrasOut = new ArrayList<Extra>();
                /* Se recorre el ArrayList y en caso de que el vector sea verdadero se añade
                 * el Extra a un arrayList de extras */
                for (int i = 0; i < listaExtras.size(); i++){
                    if (chekedExtras[i]){
                        listaExtrasOut.add( new Extra(listaExtras.get(i)));
                    }
                }
                /* Se envia un true y el arraylist con los extras seleccionados5 */
                respuesta.onRespuestaAsignarExtras(listaExtrasOut, true);
            }
        });
        /* Botón no */
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                respuesta.onRespuestaAsignarExtras(null, false);
            }
        });
        /* Se crea y se devuelve */
        return builder.create();
    }

    public interface respuestaDialogAsignarExtras{
        public void onRespuestaAsignarExtras(ArrayList<Extra> listaExtras, boolean aceptar);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        respuesta = (respuestaDialogAsignarExtras) activity;
    }
}
