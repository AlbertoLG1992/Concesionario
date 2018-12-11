package com.example.alberto.concesionario.Activities.Presupuestos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.concesionario.R;

public class VerPresupuestoCocheNuevoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto_coche_nuevo);

        /* Se extrae el presupuesto del Intent */
        Presupuesto presupuesto = (Presupuesto) getIntent().getExtras().getSerializable("presupuesto");

        /* Para comprobar que se extrae bien */
        Toast.makeText(this, presupuesto.generarPresupuesto(), Toast.LENGTH_LONG).show();
    }
}
