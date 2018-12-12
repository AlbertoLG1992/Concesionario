package com.example.alberto.concesionario.Activities.DetallesElementos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import com.example.alberto.concesionario.Activities.Presupuestos.VerPresupuestoCocheNuevoActivity;
import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Coches.TablaCoches;
import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.Dialogs.DialogAsignarExtras;
import com.example.alberto.concesionario.Dialogs.DialogBorrarCoche;
import com.example.alberto.concesionario.R;

import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class DetalleCochesNuevosActivity extends AppCompatActivity implements View.OnClickListener,
        DialogBorrarCoche.respuestaDialogBorrarCoche, DialogAsignarExtras.respuestaDialogAsignarExtras {

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
    private boolean estaModificandose;
    private TablaCoches tablaCoches;

    /* Variables para las ActivitiesForResult */
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALERIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coches_nuevos);//El layout es el mismo de add coches nuevos

        /* Se estrae el id del intent y se busca el coche con ese id */
        this.tablaCoches = new TablaCoches(this);
        this.coche = tablaCoches.extraerCoche(getIntent().getIntExtra("id", 0));
        this.estaModificandose = false;//Empieza sin modificar

        this.iniciarElementos();
        this.iniciarToolbar();
        this.rellenarElementos();
        this.cambiarEstadoModificar(this.estaModificandose);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnAddCocheNuevoFoto:{
                /* Comprueba permisos y lanza la actividad para cargar camara */
                if (checkPermission()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
            case R.id.imgBtnAddCocheNuevoGaleria:{
                /* Comprueba permisos y lanza la actividad para cargar galeria */
                if (checkPermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_IMAGE_GALERIA);
                }
                break;
            }
        }
    }

    /**
     * Cambia el estado de los elementos de la actividad dependiendo si se estan modificando
     * los campos o no
     *
     * @param seModifica :boolean
     */
    private void cambiarEstadoModificar(boolean seModifica){
        if (seModifica){
            this.imageButtonGaleria.setVisibility(View.VISIBLE);
            this.imageButtonFoto.setVisibility(View.VISIBLE);
        }else {
            this.imageButtonFoto.setVisibility(View.GONE);
            this.imageButtonGaleria.setVisibility(View.GONE);
        }
        this.edtModelo.setEnabled(seModifica);
        this.edtPrecio.setEnabled(seModifica);
        this.edtMarca.setEnabled(seModifica);
        this.edtDescripcion.setEnabled(seModifica);
    }

    /**
     * Comprueba que todos los elementos de la actividad estan rellenos
     *
     * @return boolean
     */
    private boolean comprobarCamposRellenos(){
        if (!this.edtDescripcion.getText().toString().isEmpty()){
            if (!this.edtMarca.getText().toString().isEmpty()){
                if (!this.edtPrecio.getText().toString().isEmpty()){
                    if (!this.edtModelo.getText().toString().isEmpty()){
                        return true;
                    }
                }
            }
        }
        Toast.makeText(this, "Para guardar cambios, todos los campos han de estar " +
                "rellenos", Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * Modifica el coche con los datos de los elementos de la actividad y lo actualiza
     * en la base de datos
     */
    private void modificarCoche(){
        this.coche.setFoto(((BitmapDrawable)this.imageView.getDrawable()).getBitmap());
        this.coche.setDescripcion(this.edtDescripcion.getText().toString());
        this.coche.setMarca(this.edtMarca.getText().toString());
        this.coche.setPrecio(Integer.parseInt(this.edtPrecio.getText().toString()));
        this.coche.setModelo(this.edtModelo.getText().toString());
        this.tablaCoches.modificarCoche(this.coche);
    }

    /**
     * Método que se ejecuta cuando se pulsa en alguno de los item del menú
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemModificar:{
                /* Cambia el estado de estaModificandose */
                this.estaModificandose = !this.estaModificandose;
                this.cambiarEstadoModificar(this.estaModificandose);
                return true;
            }
            case R.id.itemGuardarCambios:{
                /* Comprueba que se pueden modificar los campos del coche y lo modifica
                 * es afirmativo */
                if (this.estaModificandose) {
                    if (this.comprobarCamposRellenos()) {
                        this.estaModificandose = false;
                        this.cambiarEstadoModificar(false);
                        /* Se modifica y se envia un resultOk al main */
                        this.modificarCoche();
                        setResult(RESULT_OK);
                    }
                }else {
                    Toast.makeText(this, "Para guardar cambios, antes activa la " +
                            "opción modificar...", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            case R.id.itemCancelarCambios:{
                /* Cancela los cambios hechos en la actividad sobre el coche y devuelve a
                 * los valores de la base de datos */
                if (this.estaModificandose) {
                    this.estaModificandose = false;
                    this.cambiarEstadoModificar(false);
                    this.rellenarElementos();
                }else {
                    Toast.makeText(this, "Para descartar cambios, antes activa la " +
                            "opción modificar...", Toast.LENGTH_LONG).show();
                }
                return true;
            }
            case R.id.itemEliminar:{
                /* Llama al Dialog para preguntar si es seguro que quiere borrar el coche */
                DialogBorrarCoche dialog = new DialogBorrarCoche();
                dialog.show(getSupportFragmentManager(), "dialogo");
                return true;
            }
            case R.id.itemGenerarPresupuesto:{
                DialogAsignarExtras dialog = new DialogAsignarExtras();
                dialog.show(getSupportFragmentManager(), "dialogo extras");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que cambia el estado del menú
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.itemModificar).setEnabled(!estaModificandose);
        menu.findItem(R.id.itemEliminar).setEnabled(!estaModificandose);
        menu.findItem(R.id.itemGenerarPresupuesto).setEnabled(!estaModificandose);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Metodo para insertar el menú en el toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles_coche_nuevo, menu);
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
     * Extrae del objeto coche todos los atributos para rellenar los elementos de la actividad
     */
    private void rellenarElementos(){
        this.imageView.setImageBitmap(this.coche.getFoto());
        this.edtDescripcion.setText(this.coche.getDescripcion());
        this.edtMarca.setText(this.coche.getMarca());
        this.edtPrecio.setText(String.valueOf(this.coche.getPrecio()));
        this.edtModelo.setText(this.coche.getModelo());
    }

    /**
     * Inicia los elementos de la actividad y los enlaza con el XML y los hace
     * clickables
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

        /* CLICKABLE */
        this.imageButtonFoto.setOnClickListener(this);
        this.imageButtonGaleria.setOnClickListener(this);
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
            if ((checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                /* En caso de no haber cargado correctamente los permisos se avisa con
                 * un Toast y se piden */
                Toast.makeText(getApplicationContext(), "Error al cargar permisos", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 100);
                return false;
            } else {
                /* En caso de todos los permisos correctos se notifica */
                Toast.makeText(getApplicationContext(), "Todos los permisos se han cargado correctamente", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return true;
    }

    /**
     * Función que recoje un Bitmap y lo redimensiona
     *
     * @param source :Bitmap
     * @return Bitmap
     */
    public Bitmap transform(Bitmap source) {
        int sizeWidth = source.getWidth();
        int sizeHeight = sizeWidth / 2 + 20;
        int x = 0;
        int y = 0;
        Bitmap result = Bitmap.createBitmap(source, x, y, sizeWidth, sizeHeight);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /* Recoge un un bitmap del ResultSet y lo carga al imageView */
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");
            imageBitmap = transform(imageBitmap);
            this.imageView.setImageBitmap(imageBitmap);
        }
        if (requestCode == REQUEST_IMAGE_GALERIA && resultCode == RESULT_OK){
            /* Recoge la direccion donde se encuentra la imagen */
            Uri uri = data.getData();
            try {
                /* Transforma el enlace en bitmap y lo carga a en el imageView */
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = transform(bitmap);
                this.imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que se ejecuta al pulsar sobre una de las opciones del dialog BorrarCoche
     *
     * @param seBorra :boolean
     */
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

    @Override
    public void onRespuestaAsignarExtras(ArrayList<Extra> listaExtras, boolean aceptar) {
        if (aceptar){
            Toast.makeText(this, "Generando...", Toast.LENGTH_LONG).show();

            Presupuesto presupuesto = new Presupuesto(this.coche, listaExtras);
            Intent intent = new Intent(getApplicationContext(), VerPresupuestoCocheNuevoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("presupuesto", presupuesto);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
