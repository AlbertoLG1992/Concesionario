package com.example.alberto.concesionario;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alberto.concesionario.Adaptadores.AdapterCoches;
import com.example.alberto.concesionario.Adaptadores.AdapterExtra;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    /** ELEMENTOS **/
    private BottomNavigationView navigationMenu;
    private Toolbar toolbar;
    private TextView textView;
    private ListView listViewMain;
    private FloatingActionButton floatBtn;

    /** VARIABLES **/
    /* navigationActual sirve para que cuando se pulsa un item de onNavigationItemSelected se
     * señale como item actual y en onPrepareOptionsMenu se pueda activar o desactivar
     * item del menu en señal del actual */
    private String navigationActual = "";
    AdapterCoches adapterCoches;
    AdapterExtra adapterExtra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarElementos();
        this.iniciarToolbar();

        /* Para que comience en Coche Nuevos */
        this.navigationMenu.setSelectedItemId(R.id.navigationCochesNuevos);

        /* Listener de FloatingActionButton */
        this.floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (navigationActual){
                    case "Coches Nuevos":{
                        /* Se habre la actividad de AddCochesNuevosActivity */
                        Intent intent = new Intent(getApplicationContext(), AddCochesNuevosActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    }
                    case "Coches Usados":{

                        break;
                    }
                    case "Extras":{
                        Snackbar.make(view, "Here's a Snackbar in Extras", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                    }
                }
            }
        });
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
                this.cargarAdaptadorCochesNuevos();
                return true;
            }
            case R.id.navigationCochesUsados:{
                this.navigationActual = "Coches Usados";
                this.textView.setText(navigationActual);
                this.cargarAdaptadorCochesUsados();
                return true;
            }
            case R.id.navigationExtras:{
                this.navigationActual = "Extras";
                this.textView.setText(navigationActual);
                this.cargarAdaptadorExtras();
                return true;
            }
        }
        return false;
    }

    /**
     * Carga el adaptador de Extras
     */
    public void cargarAdaptadorExtras() {
        this.adapterExtra = new AdapterExtra(this, getApplicationContext());
        this.listViewMain.setAdapter(this.adapterExtra);
    }

    /**
     * Carga el adaptador de coches usados
     */
    public void cargarAdaptadorCochesUsados() {
        this.adapterCoches = new AdapterCoches(this, getApplicationContext(), false);
        this.listViewMain.setAdapter(this.adapterCoches);
    }

    /**
     * Carga el adaptador de coches nuevos
     */
    public void cargarAdaptadorCochesNuevos(){
        this.adapterCoches = new AdapterCoches(this, getApplicationContext(), true);
        this.listViewMain.setAdapter(adapterCoches);

    }

    /**
     * Método que cambia el estado del menú
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        /* Item Coche Nuevos */
        if (this.navigationActual.equals("Coches Nuevos")){
            menu.findItem(R.id.itemCochesNuevos).setEnabled(false);
        }else {
            menu.findItem(R.id.itemCochesNuevos).setEnabled(true);
        }

        /* Item Coche Usados */
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
                /* Activa el item de Coche Nuevos en navigationMenu */
                this.navigationMenu.setSelectedItemId(R.id.navigationCochesNuevos);
                break;
            }
            case R.id.itemCochesUsados:{
                /* Activa el item de Coche Usados en navigationMenu */
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
        this.listViewMain = (ListView) findViewById(R.id.listViewMain);
        this.floatBtn = (FloatingActionButton) findViewById(R.id.floatBtnMain);

        /* CLICKABLES */
        this.navigationMenu.setOnNavigationItemSelectedListener(this);
    }
}
