package com.example.alberto.concesionario.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alberto.concesionario.R;

public class DialogNombreCliente extends DialogFragment {

    respuestaDialogNombreCliente respuesta;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_nombre_cliente, null);

        final EditText edtNombre = (EditText)view.findViewById(R.id.edtNombre);

        builder.setTitle("Nombre del cliente");
        builder.setView(view);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!edtNombre.getText().toString().isEmpty()){
                    respuesta.onRespuestaDialogNombreCliente(edtNombre.getText().toString());
                }else {
                    Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public interface respuestaDialogNombreCliente{
        public void onRespuestaDialogNombreCliente(String nombre);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogNombreCliente) activity;
    }
}
