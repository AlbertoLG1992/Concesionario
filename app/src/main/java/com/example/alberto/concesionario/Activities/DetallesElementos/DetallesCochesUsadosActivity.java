package com.example.alberto.concesionario.Activities.DetallesElementos;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alberto.concesionario.Activities.Presupuestos.Presupuesto;
import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Coches.TablaCoches;
import com.example.alberto.concesionario.BaseDeDatos.Extras.TablaExtras;
import com.example.alberto.concesionario.Dialogs.DialogBorrarCoche;
import com.example.alberto.concesionario.Dialogs.DialogNombreCliente;
import com.example.alberto.concesionario.Dialogs.DialogVerExtraDeCoche;
import com.example.alberto.concesionario.PDF.GenerarPDF;
import com.example.alberto.concesionario.R;

public class DetallesCochesUsadosActivity extends AppCompatActivity implements DialogBorrarCoche.respuestaDialogBorrarCoche, DialogNombreCliente.respuestaDialogNombreCliente{

    /** ELEMENTOS **/
    private Toolbar toolbar;
    private ImageButton imageButtonFoto,
            imageButtonGaleria;
    private ImageView imageView;
    private EditText edtMarca,
            edtModelo,
            edtPrecio,
            edtDescripcion;

    /* Variables generales */
    private Coche coche;
    private TablaCoches tablaCoches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coches_nuevos);

        /* Se estrae el id del intent y se busca el coche con ese id */
        this.tablaCoches = new TablaCoches(this);
        this.coche = tablaCoches.extraerCoche(getIntent().getIntExtra("id", 0));

        this.iniciarElementos();
        this.iniciarToolbar();
        this.rellenarElementos();
        this.desactivarCampos();
    }

    /**
     * Rellena los campos con el coche extraido
     */
    private void rellenarElementos() {
        this.imageView.setImageBitmap(this.coche.getFoto());
        this.edtDescripcion.setText(this.coche.getDescripcion());
        this.edtMarca.setText(this.coche.getMarca());
        this.edtPrecio.setText(String.valueOf(this.coche.getPrecio()));
        this.edtModelo.setText(this.coche.getModelo());
    }

    /**
     * Método que se ejecuta cuando se pulsa en alguno de los item del menú
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.itemExtras:{
                DialogVerExtraDeCoche dialog = new DialogVerExtraDeCoche();
                dialog.setCoche(this.coche);
                dialog.show(getSupportFragmentManager(), "dialogo");
                break;
            }
            case R.id.itemEliminar:{
                /* Llama al Dialog para preguntar si es seguro que quiere borrar el coche */
                DialogBorrarCoche dialog = new DialogBorrarCoche();
                dialog.show(getSupportFragmentManager(), "dialogo");
                return true;
            }
            case R.id.itemReservar:{
                DialogNombreCliente dialog = new DialogNombreCliente();
                dialog.show(getSupportFragmentManager(), "dialog");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRespuestaDialogNombreCliente(String nombre) {
        this.generarPresupuesto(nombre);
    }

    private void generarPresupuesto(String nombre){
        TablaExtras tablaExtras = new TablaExtras(getApplicationContext());
        Presupuesto presupuesto = new Presupuesto(this.coche, tablaExtras.verExtrasDeCoche(this.coche));

        GenerarPDF pdf;
        pdf = new GenerarPDF(getApplicationContext());
        pdf.openDocument();
        pdf.addMetaData("Presupuesto", "Presupuesto de un coche", "Safiro Auto");
        pdf.addTtitulo("Presupuesto de Ocasión", presupuesto.getNombreCoche());
        pdf.addNombreCliente(nombre);
        pdf.crearTablaPresupuesto(presupuesto);
        pdf.closeDocument();

        //TODO ENVIAR PDF
    }

    /**
     * Desactiva todos los campos para evitar que se modifiquen
     */
    private void desactivarCampos() {
        this.edtModelo.setEnabled(false);
        this.edtPrecio.setEnabled(false);
        this.edtMarca.setEnabled(false);
        this.edtDescripcion.setEnabled(false);
        this.imageButtonFoto.setVisibility(View.GONE);
        this.imageButtonGaleria.setVisibility(View.GONE);
    }

    /**
     * Metodo para insertar el menú en el toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalle_coche_usado, menu);
        return true;
    }

    /**
     * Método que escribe el titulo del toolbar y lo muestra
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle("Detalles");
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Inicia los elementos de la actividad y lo senlaza con el XML
     */
    private void iniciarElementos() {
        /* XML */
        this.toolbar = (Toolbar) findViewById(R.id.toolbarAddCoches);
        this.imageButtonFoto = (ImageButton) findViewById(R.id.imgBtnAddCocheNuevoFoto);
        this.imageButtonGaleria = (ImageButton) findViewById(R.id.imgBtnAddCocheNuevoGaleria);
        this.imageView = (ImageView) findViewById(R.id.imgvAddCochesNuevos);
        this.edtModelo = (EditText) findViewById(R.id.edtModeloNuevo);
        this.edtDescripcion = (EditText) findViewById(R.id.edtDescripcionNuevo);
        this.edtPrecio = (EditText) findViewById(R.id.edtPrecioNuevo);
        this.edtMarca = (EditText) findViewById(R.id.edtMarcaNuevo);
    }

    @Override
    public void onRespuestaBorrarCoche(boolean seBorra) {
        if (seBorra){
            /* En caso de quere borrarlo, se elimina, se notifica y se envia un
             * resultOk y finaliza la actividad */
            this.tablaCoches.eliminarCoche(this.coche);
            Toast.makeText(this, "Borrado...", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        }
    }
}
