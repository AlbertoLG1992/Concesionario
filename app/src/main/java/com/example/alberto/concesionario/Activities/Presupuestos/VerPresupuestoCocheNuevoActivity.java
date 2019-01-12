package com.example.alberto.concesionario.Activities.Presupuestos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.concesionario.Dialogs.DialogDatosCliente;
import com.example.alberto.concesionario.PDF.GenerarPDF;
import com.example.alberto.concesionario.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class VerPresupuestoCocheNuevoActivity extends AppCompatActivity implements View.OnClickListener, DialogDatosCliente.respuestaDialogDatosCliente {

    /** ELEMENTOS **/
    private PDFView pdfView;
    private Button btnRecargar;
    private FloatingActionButton floatButton;

    /* Atributos */
    private GenerarPDF pdf;
    private Presupuesto presupuesto;
    private DatosCliente datosCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuesto_coche_nuevo);



        /* Se extrae el presupuesto del Intent */
        this.presupuesto = (Presupuesto) getIntent().getExtras().getSerializable("presupuesto");

        this.iniciarElementos();
        this.btnRecargar.setVisibility(View.GONE);
        this.crearGenerarPdf();

        this.floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDatosCliente dialog = new DialogDatosCliente();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    /**
     * Se enlazan los elementos de la actividad con el XML
     */
    private void iniciarElementos(){
        this.pdfView = (PDFView) findViewById(R.id.visorPdf);
        this.btnRecargar = (Button)findViewById(R.id.btnRecargar);
        this.floatButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        this.btnRecargar.setOnClickListener(this);
    }

    /**
     * Genera un PDF con el presupuesto
     */
    private void crearGenerarPdf(){
        /* Pregunta por los permisos, en caso de que no los tenga aparecera un botón en la pantalla
         * para recargar el pdf */
        if (checkPermission()) {
            this.pdf = new GenerarPDF(getApplicationContext());
            this.pdf.openDocument();
            this.pdf.addMetaData("Presupuesto", "Presupuesto de un coche", "Safiro Auto");
            this.pdf.addTtitulo("Presupuesto", this.presupuesto.getNombreCoche());

            /* En caso de que el cliente este creado, es decir, que se haya rellenado en el dialog
             * se añadira el parrafo con los datos del cliente */
            if (this.datosCliente != null){
                this.pdf.addDatosCliente(this.datosCliente);
            }
            this.pdf.crearTablaPresupuesto(this.presupuesto);

            this.pdf.closeDocument();
            this.cargarPdf();
        }else{
            /* En caso de que se tuviesen tuviesemos permisos se muestra un boton
             * para recargar */
            this.btnRecargar.setVisibility(View.VISIBLE);
        }
    }

    private void cargarPdf(){
        //File file = new File(this.pdf.viewPDF());
        pdfView.fromFile(new File(this.pdf.verPathPDF()))
                .enableSwipe(true)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
    }

    /**
     * Método que se ejecuta al aceptar corectamente el Dialog
     *
     * @param datosCliente :DatosCliente
     */
    @Override
    public void onRespuestaDialogDatosCliente(DatosCliente datosCliente) {
        this.datosCliente = datosCliente;
        this.crearGenerarPdf();
        this.enviarCorreo();
    }

    private void enviarCorreo(){
        String[] TO = {this.datosCliente.getEmail()};
        ArrayList<Uri> listasUris = new ArrayList<Uri>();
        listasUris.add(pdf.verUriPDF());

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Presupuesto Safiro Auto");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listasUris);
        intent.putExtra(Intent.EXTRA_TEXT, "Mensaje generado por SafiroApp");

        /* Para evitar las politicas de seguridad de android */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        intent.setType("*/*");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        /* En caso de que los permisos no se carguen correctamente el boton
         * que aparecerá en mitad de la pantalla servirá para recargar la actividad
         * de nuevo*/
        this.crearGenerarPdf();
        this.btnRecargar.setVisibility(View.GONE);
    }

    /**
     * Método que revisa si los permisos son correctos y en caso negativo pide dichos
     * permisos
     *
     * @return true en caso de que los permisos esten aceptados y false en caso contrario
     */
    private boolean checkPermission() {
        /* Se compureba de que la SDK es superior a marshmallow, pues si es inferior no es necesario
         * pedir permisos */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                /* En caso de no haber cargado correctamente los permisos se avisa con
                 * un Toast y se piden */
                Toast.makeText(getApplicationContext(), "Error al cargar permisos", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            } else {
                /* En caso de todos los permisos correctos se notifica */
                Log.e("Permisos correctos", toString());
                return true;
            }
        }
        return true;
    }
}
