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

import com.example.alberto.concesionario.Activities.Presupuestos.DatosCliente;
import com.example.alberto.concesionario.R;

public class DialogDatosCliente extends DialogFragment {

    respuestaDialogDatosCliente respuesta;
    private EditText    edtNombre,
                        edtApellidos,
                        edtTlf,
                        edtEmail,
                        edtDireccion,
                        edtPoblacion,
                        edtFecha;
    private View view;
    private DatosCliente datosCliente;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        this.view = inflater.inflate(R.layout.dialog_datos_cliente, null);

        this.asignarElementos();

        builder.setTitle("Datos del Cliente");
        builder.setView(view);
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (comprobarDatosRellenos()){
                    extraerDatosCliente();
                    respuesta.onRespuestaDialogDatosCliente(datosCliente);
                }else {
                    Toast.makeText(getContext(), "Todos los datos han de estar rellenos",
                            Toast.LENGTH_LONG).show();
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

    public interface respuestaDialogDatosCliente{
        public void onRespuestaDialogDatosCliente(DatosCliente datosCliente);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogDatosCliente) activity;
    }

    /**
     * Extrae los datos de los editText y crea un DatosCliente con ellos
     */
    private void extraerDatosCliente(){
        this.datosCliente = new DatosCliente(this.edtNombre.getText().toString(),
                this.edtApellidos.getText().toString(), this.edtTlf.getText().toString(),
                this.edtEmail.getText().toString(), this.edtDireccion.getText().toString(),
                this.edtPoblacion.getText().toString(), this.edtFecha.getText().toString());
    }

    /**
     * Comprueba que todos los datos estan rellenos
     *
     * @return boolean
     */
    private boolean comprobarDatosRellenos(){
        if (!this.edtNombre.getText().toString().isEmpty()){
            if (!this.edtApellidos.getText().toString().isEmpty()){
                if (!this.edtDireccion.getText().toString().isEmpty()){
                    if (!this.edtEmail.getText().toString().isEmpty()){
                        if (!this.edtTlf.getText().toString().isEmpty()){
                            if (!this.edtPoblacion.getText().toString().isEmpty()){
                                if (!this.edtFecha.getText().toString().isEmpty()){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Asigna los elementos del layout del dialog al xml
     */
    private void asignarElementos(){
        this.edtNombre = (EditText)this.view.findViewById(R.id.edtNombre);
        this.edtApellidos = (EditText)this.view.findViewById(R.id.edtApellidos);
        this.edtTlf = (EditText)this.view.findViewById(R.id.edtTlf);
        this.edtEmail = (EditText)this.view.findViewById(R.id.edtEmail);
        this.edtDireccion = (EditText)this.view.findViewById(R.id.edtDireccion);
        this.edtPoblacion = (EditText)this.view.findViewById(R.id.edtPoblacion);
        this.edtFecha = (EditText)this.view.findViewById(R.id.edtFecha);
    }
}
