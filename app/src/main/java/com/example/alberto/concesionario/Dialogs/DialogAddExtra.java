package com.example.alberto.concesionario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.R;

public class DialogAddExtra extends DialogFragment {
    private TextView edtNombre,
                    edtDescrip,
                    edtPrecio;

    respuestaDialogAddExtras respuesta;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_extra, null);

        /* Se asignan los textView los ids del XML */
        this.edtNombre = (TextView) view.findViewById(R.id.edtNombreAddExtra);
        this.edtDescrip = (TextView)view.findViewById(R.id.edtDescripAddExtra);
        this.edtPrecio = (TextView)view.findViewById(R.id.edtPrecioAddExtra);

        builder.setView(view)
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Se comprueban que todos los campos estan rellenos */
                        if (!edtNombre.getText().toString().isEmpty() &&
                                !edtDescrip.getText().toString().isEmpty() &&
                                !edtPrecio.getText().toString().isEmpty()){
                            /* Se crea el extra */
                            Extra extra = new Extra(edtNombre.getText().toString(),
                                    edtDescrip.getText().toString(),
                                    Integer.parseInt(edtPrecio.getText().toString()));
                            /* Se pasa al main */
                            respuesta.onRespuestaAddExtras(true, extra);
                        }else {
                            /* En caso de que los campos no esten rellenos se devuelve false */
                            respuesta.onRespuestaAddExtras(false, null);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public interface respuestaDialogAddExtras{
        public void onRespuestaAddExtras(boolean aceptar, Extra extra);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogAddExtras) activity;
    }
}
