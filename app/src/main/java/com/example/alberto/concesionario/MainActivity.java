package com.example.alberto.concesionario;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /** ELEMENTOS **/
    private BottomNavigationView navigationMenu;
    private Toolbar toolbar;
    private TextView textView;

    /** VARIABLES **/
    /* navigationActual sirve para que cuando se pulsa un item de onNavigationItemSelected se
     * señale como item actual y en onPrepareOptionsMenu se pueda activar o desactivar
     * item del menu en señal del actual */
    private String navigationActual = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarElementos();
        this.iniciarToolbar();

        /* Para que comience en Coches Nuevos */
        this.navigationMenu.setSelectedItemId(R.id.navigationCochesNuevos);
    }

    /**
     * Método iniciado al pulsar sobre un elemento del BottomNavigationView
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigationCochesNuevos:{
                this.navigationActual = "Coches Nuevos";
                this.textView.setText(navigationActual);
                return true;
            }
            case R.id.navigationCochesUsados:{
                this.navigationActual = "Coches Usados";
                this.textView.setText(navigationActual);
                return true;
            }
            case R.id.navigationExtras:{
                this.navigationActual = "Extras";
                this.textView.setText(navigationActual);
                return true;
            }
        }
        return false;
    }

    /**
     * Método que cambia el estado del menú
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        /* Item Coches Nuevos */
        if (this.navigationActual.equals("Coches Nuevos")){
            menu.findItem(R.id.itemCochesNuevos).setEnabled(false);
        }else {
            menu.findItem(R.id.itemCochesNuevos).setEnabled(true);
        }

        /* Item Coches Usados */
        if (this.navigationActual.equals("Coches Usados")){
            menu.findItem(R.id.itemCochesUsados).setEnabled(false);
        }else {
            menu.findItem(R.id.itemCochesUsados).setEnabled(true);
        }

        /* Item Extras */
        if (this.navigationActual.equals("Extras")){
            menu.findItem(R.id.itemExtras).setEnabled(false);
        }else {
            menu.findItem(R.id.itemExtras).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
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
            case R.id.itemCochesNuevos:{
                /* Activa el item de Coches Nuevos en navigationMenu */
                this.navigationMenu.setSelectedItemId(R.id.navigationCochesNuevos);
                break;
            }
            case R.id.itemCochesUsados:{
                /* Activa el item de Coches Usados en navigationMenu */
                this.navigationMenu.setSelectedItemId(R.id.navigationCochesUsados);
                break;
            }
            case R.id.itemExtras:{
                /* Activa el item de Extras en navigationMenu */
                this.navigationMenu.setSelectedItemId(R.id.navigationExtras);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Método que escribe el titulo del toolbar y lo muestra
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle("Concesionario");
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Inicia los elementos de la actividad y los enlaza con el XML y los hace
     * clickables
     */
    private void iniciarElementos(){
        /* XML */
        this.navigationMenu = (BottomNavigationView)findViewById(R.id.navigation);
        this.toolbar = (Toolbar)findViewById(R.id.toolbarMain);
        this.textView = (TextView) findViewById(R.id.textView);

        /* CLICKABLES */
        this.navigationMenu.setOnNavigationItemSelectedListener(this);
    }
}
