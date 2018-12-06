package com.example.alberto.concesionario;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

public class AddCochesNuevosActivity extends AppCompatActivity implements View.OnClickListener {

    /** ELEMENTOS **/
    private Toolbar toolbar;
    private ImageButton imageButton;
    private ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coches_nuevos);

        this.iniciarElementos();
        this.iniciarToolbar();



    }

    @Override
    public void onClick(View v) {
        /* No hay que hacer comparativas dado que solo exite el boton como objeto
         * clicklable, los menus tienen su propio Listener*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.getParcelable("data");

            //TODO RECOGER IMAGEN CON MÁS CALIDAD

            imageBitmap = transform(imageBitmap);

            //Picasso
                    //.with(getApplicationContext())
                    //.load(urifoto)
                    //.resize(100, 200)
                    //.into(this.imageView);


            this.imageView.setImageBitmap(imageBitmap);

        }
    }


    /**
     * Función que recoje un Bitmap y lo redimensiona
     *
     * @param source
     * @return
     */
    public Bitmap transform(Bitmap source) {
        //int size = Math.min(source.getWidth(), source.getHeight());
        int sizeWidth = source.getWidth();
        int sizeHeight = sizeWidth / 2;
        //int x = (source.getWidth() - size) / 2;
        int x = 0;
        //int y = (source.getHeight() - size) / 2;
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

                break;
            }
            case R.id.itemReestAddCocheNuevo:{

                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
        this.imageButton = (ImageButton)findViewById(R.id.imageButtonAddCocheNuevo);
        this.imageView = (ImageView)findViewById(R.id.imgvAddCochesNuevos);

        /* CLICKABLE */
        this.imageButton.setOnClickListener(this);
    }
}
