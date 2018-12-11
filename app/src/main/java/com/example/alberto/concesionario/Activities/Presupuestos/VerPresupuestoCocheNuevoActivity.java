package com.example.alberto.concesionario.Activities.Presupuestos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.concesionario.PDF.GenerarPDF;
import com.example.alberto.concesionario.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VerPresupuestoCocheNuevoActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto_coche_nuevo);

        /* Se extrae el presupuesto del Intent */
        Presupuesto presupuesto = (Presupuesto) getIntent().getExtras().getSerializable("presupuesto");

        /* Prueba para comprobar que el pdf se crea y se visualiza dentro de la actividad */

        GenerarPDF pdf = new GenerarPDF(getApplicationContext());
        pdf.openDocument();
        //pdf.closeDocument();
        File file = new File(pdf.viewPDF());

        pdfView=(PDFView)findViewById(R.id.visorPdf);
        pdfView.fromFile(file).enableDoubletap(true).load();

    }
}
