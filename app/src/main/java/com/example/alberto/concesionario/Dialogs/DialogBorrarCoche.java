package com.example.alberto.concesionario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogBorrarCoche extends DialogFragment {

    respuestaDialogBorrarCoche respuesta;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("ATENCIÓN")
                .setMessage("¿Seguro que desea borrar el coche?\n" +
                        "Despues no habrá vuelta atrás...")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        respuesta.onRespuestaBorrarCoche(true);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        respuesta.onRespuestaBorrarCoche(false);
                    }
                });
        return builder.create();
    }

    public interface respuestaDialogBorrarCoche {
        public void onRespuestaBorrarCoche(boolean seBorra);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogBorrarCoche) activity;
    }
}
