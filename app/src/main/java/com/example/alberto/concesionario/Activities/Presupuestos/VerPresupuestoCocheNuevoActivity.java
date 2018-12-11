package com.example.alberto.concesionario.Activities.Presupuestos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.alberto.concesionario.R;

public class VerPresupuestoCocheNuevoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto_coche_nuevo);

        Presupuesto presupuesto = (Presupuesto) getIntent().getExtras().getSerializable("presupuesto");


    }
}
