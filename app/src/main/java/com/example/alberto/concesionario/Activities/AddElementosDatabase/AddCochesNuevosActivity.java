package com.example.alberto.concesionario.Activities.AddElementosDatabase;

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

import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Coches.TablaCoches;
import com.example.alberto.concesionario.R;

import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddCochesNuevosActivity extends AppCompatActivity implements View.OnClickListener {

    /** ELEMENTOS **/
    private Toolbar toolbar;
    private ImageButton imageButtonFoto,
                        imageButtonGaleria;
    private ImageView imageView;
    private EditText    edtMarcaNuevo,
                        edtModeloNuevo,
                        edtPrecioNuevo,
                        edtDescripcionNuevo;

    /* Variables generales */
    private boolean cambiaFoto = false;

    /* Variables para las ActivitiesForResult */
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALERIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coches_nuevos);

        this.iniciarElementos();
        this.iniciarToolbar();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /* Recoge un un bitmap del ResultSet y lo carga al imageView */
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");
            imageBitmap = transform(imageBitmap);
            this.imageView.setImageBitmap(imageBitmap);
            this.cambiaFoto = true;
        }
        if (requestCode == REQUEST_IMAGE_GALERIA && resultCode == RESULT_OK){
            /* Recoge la direccion donde se encuentra la imagen */
            Uri uri = data.getData();
            try {
                /* Transforma el enlace en bitmap y lo carga a en el imageView */
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = transform(bitmap);
                this.imageView.setImageBitmap(bitmap);
                this.cambiaFoto = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Función que recoje un Bitmap y lo redimensiona
     *
     * @param source
     * @return
     */
    public Bitmap transform(Bitmap source) {
        int sizeWidth = source.getWidth();
        int sizeHeight = sizeWidth / 2 + 20;
        int x = 0;
        int y = 0;
        Bitmap result = Bitmap.createBitmap(source, x, y, sizeWidth, sizeHeight);
        return result;
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
            case R.id.itemGuardarAddCocheNuevo:{
                if (checkDatosRellenos()){
                    /* Se crea un coche para guardarlo en la base de datos */
                    Coche coche = new Coche(this.edtMarcaNuevo.getText().toString(),
                            this.edtModeloNuevo.getText().toString(),
                            Integer.parseInt(this.edtPrecioNuevo.getText().toString()),
                            this.edtDescripcionNuevo.getText().toString(),
                            ((BitmapDrawable)this.imageView.getDrawable()).getBitmap(),
                            true);
                    /* Se abre la base de datos y se inserta en ella el coche nuevo */
                    TablaCoches tablaCoches = new TablaCoches(this);
                    tablaCoches.addCoche(coche);

                    /* Se notifica que el coche se ha añadido correctamente */
                    Toast.makeText(this, "Coche añadido correctamente", Toast.LENGTH_LONG).show();

                    /* Se envia a MainActivity un RESULT_OK y se cierra actividad */
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            }
            case R.id.itemReestAddCocheNuevo:{
                /* Devuelve todos los campos a los establecidos por defecto */
                this.edtMarcaNuevo.setText("");
                this.edtPrecioNuevo.setText("");
                this.edtDescripcionNuevo.setText("");
                this.edtModeloNuevo.setText("");
                this.imageView.setImageResource(R.mipmap.ic_launcher);
                this.cambiaFoto = false;
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Comprueba que la foto ha sido cambiada y todos los campos estan rellenos
     * @return
     */
    private boolean checkDatosRellenos(){
        if (!this.edtModeloNuevo.getText().toString().isEmpty()){
            if (!this.edtDescripcionNuevo.getText().toString().isEmpty()){
                if (!this.edtPrecioNuevo.getText().toString().isEmpty()){
                    if (!this.edtMarcaNuevo.getText().toString().isEmpty()){
                        if (cambiaFoto){
                            return true;
                        }else{
                            Toast.makeText(this, "Cambia la foto", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
            }
        }
        Toast.makeText(this, "Todos los campos tienen que estar rellenos", Toast.LENGTH_LONG).show();
        return false;
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
     * Metodo para insertar el menú en el toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_coche_nuevo, menu);
        return true;
    }

    /**
     * Método que escribe el titulo del toolbar y lo muestra
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle("Añade un coche nuevo");
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Inicia los elementos de la actividad y los enlaza con el XML y los hace
     * clickables
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarAddCoches);
        this.imageButtonFoto = (ImageButton)findViewById(R.id.imgBtnAddCocheNuevoFoto);
        this.imageButtonGaleria = (ImageButton)findViewById(R.id.imgBtnAddCocheNuevoGaleria);
        this.imageView = (ImageView)findViewById(R.id.imgvAddCochesNuevos);
        this.edtModeloNuevo = (EditText)findViewById(R.id.edtModeloNuevo);
        this.edtDescripcionNuevo = (EditText)findViewById(R.id.edtDescripcionNuevo);
        this.edtPrecioNuevo = (EditText)findViewById(R.id.edtPrecioNuevo);
        this.edtMarcaNuevo = (EditText)findViewById(R.id.edtMarcaNuevo);

        /* CLICKABLE */
        this.imageButtonFoto.setOnClickListener(this);
        this.imageButtonGaleria.setOnClickListener(this);
    }
}
