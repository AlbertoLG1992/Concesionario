package com.example.alberto.concesionario;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alberto.concesionario.Activities.AddElementosDatabase.AddCochesNuevosActivity;
import com.example.alberto.concesionario.Activities.AddElementosDatabase.AddCochesUsadosActivity;
import com.example.alberto.concesionario.Activities.DetallesElementos.DetalleCochesNuevosActivity;
import com.example.alberto.concesionario.Activities.DetallesElementos.DetallesCochesUsadosActivity;
import com.example.alberto.concesionario.Activities.Maps.MapsActivity;
import com.example.alberto.concesionario.Adaptadores.AdapterCoches;
import com.example.alberto.concesionario.Adaptadores.AdapterExtra;
import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;
import com.example.alberto.concesionario.BaseDeDatos.Extras.TablaExtras;
import com.example.alberto.concesionario.Dialogs.DialogAddExtra;
import com.example.alberto.concesionario.Dialogs.DialogBorrarCoche;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        DialogAddExtra.respuestaDialogAddExtras, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        DialogBorrarCoche.respuestaDialogBorrarCoche {

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
    private AdapterCoches adapterCoches;
    private AdapterExtra adapterExtra;
    private Extra extraBorrar;

    /* Variables para las ActivitiesForResult */
    static final int REQUEST_COCHE_NUEVO = 1;
    static final int REQUEST_COCHE_USADO = 2;


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
                /* El FloatingActionButton cambia su uso dependiendo en que navigation este situado,
                 * por este motivo se comprueba en que navigation se encuentra */
                switch (navigationActual){
                    case "Coches Nuevos":{
                        /* Se habre la actividad de AddCochesNuevosActivity */
                        Intent intent = new Intent(getApplicationContext(), AddCochesNuevosActivity.class);
                        startActivityForResult(intent, REQUEST_COCHE_NUEVO);
                        break;
                    }
                    case "Coches Usados":{
                        /* Se habre la actividad de AddCochesUsadosActivity */
                        Intent intent = new Intent(getApplicationContext(), AddCochesUsadosActivity.class);
                        startActivityForResult(intent, REQUEST_COCHE_USADO);
                        break;
                    }
                    case "Extras":{
                        /* Se habre Dialog AddExtra */
                        DialogAddExtra dialogAddExtra = new DialogAddExtra();
                        dialogAddExtra.show(getSupportFragmentManager(), "dialogo");
                        break;
                    }
                }
            }
        });
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
        this.listViewMain.setOnItemClickListener(this);
        this.listViewMain.setOnItemLongClickListener(this);
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
     * Metodo para insertar el menú en el toolbar
     *
     * @param menu :Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Método que se ejecuta cuando se pulsa en alguno de los item del menú
     *
     * @param item :MenuItem
     * @return boolean
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
            case R.id.itemVerUbicación:{
                /* Abre la actividad MapsActivity */
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que cambia el estado del menú
     *
     * @param menu :Menu
     * @return boolean
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
     * Método iniciado al pulsar sobre un elemento del BottomNavigationView
     *
     * @param menuItem :MenuItem
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        /* Cada vez que se pulsa sobre un item se indica en el string navigationActual el
         * navigation en el cual se encuentra y se muestra en el textView de la actividad,
         * despues se carga el adaptador correspondiente */
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
     * Metodo que se ejecuta al pulsar normal sobre el listView
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (this.navigationActual){
            case "Coches Nuevos":{
                /* Crea un intent para abrir la actividad DetalleCochesNuevos y envia el id
                 * escogido a dicha actividad */
                Intent intent = new Intent(getApplicationContext(), DetalleCochesNuevosActivity.class);
                intent.putExtra("id", (int)this.adapterCoches.getItemId(position));
                startActivityForResult(intent, REQUEST_COCHE_NUEVO);
                break;
            }
            case "Coches Usados":{
                /* Crea un intent para abrir la actividad DetallesCochesUsados y envia el id
                 * escogido a dicha actividad */
                Intent intent = new Intent(getApplicationContext(), DetallesCochesUsadosActivity.class);
                intent.putExtra("id", (int)this.adapterCoches.getItemId(position));
                startActivityForResult(intent, REQUEST_COCHE_USADO);
                break;
            }
        }
    }

    /**
     * Método iniciado al volver de una actividad iniciada con ActivityForResult
     *
     * @param requestCode :int
     * @param resultCode :int
     * @param data :Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Se comprueba que haya vuelto correctamente y en caso afirmativo se carga su
         * adaptador pulsando en el navigation que corresponda */
        if ((requestCode == REQUEST_COCHE_NUEVO) && (resultCode == RESULT_OK)){
            this.navigationMenu.setSelectedItemId(R.id.navigationCochesNuevos);
        }
        if ((requestCode == REQUEST_COCHE_USADO) && (resultCode == RESULT_OK)){
            this.navigationMenu.setSelectedItemId(R.id.navigationCochesUsados);
        }
    }

    /**
     * Método que se ejecuta al pulsar en longClick sobre el listView
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        /* Desde el ItemLongClick solo se pueden borrar los extras, por lo que
         * se comprueba que sea uno, en caso afirmativo se crea un Dialog para confirmación */
        if (this.navigationActual.equals("Extras")){
            this.extraBorrar = (Extra)this.adapterExtra.getItem(position);
            DialogBorrarCoche dialog = new DialogBorrarCoche();
            dialog.setQueBorrar("extra");
            dialog.show(getSupportFragmentManager(), "DialogBorrarCoche");
            return true;
        }
        return false;
    }

    /**
     * Método que se ejecuta al volver del dialogBorrarCoche
     *
     * @param seBorra :boolean
     */
    @Override
    public void onRespuestaBorrarCoche(boolean seBorra) {
        if (seBorra){
            /* Si se pulsa que se quiere borrar en primer lugar es necesario comprobar que
             * no existan dependencias en los coches usados, en caso de que no existan dependencias
             * se borra sin problema */
            TablaExtras tablaExtras = new TablaExtras(getApplicationContext());
            if (!tablaExtras.existenDependencias(this.extraBorrar)){
                tablaExtras.eliminarExtra(this.extraBorrar);
                Toast.makeText(this, "Borrado...", Toast.LENGTH_LONG).show();
                this.navigationMenu.setSelectedItemId(R.id.navigationExtras);
            }else{
                Toast.makeText(this, "No se puede borrar, existen dependencias",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Método iniciado al volver de DialogAddExtra
     *
     * @param aceptar :boolean
     * @param extra :Extra
     */
    @Override
    public void onRespuestaAddExtras(boolean aceptar, Extra extra) {
        if (aceptar){
            /* Se abre la base de datos en la tabla de extras y se añade el extra devuelto por
             * el dialog, despues se recarga de nuevo el listView */
            TablaExtras tablaExtras = new TablaExtras(this);
            tablaExtras.addExtra(extra);
            this.navigationMenu.setSelectedItemId(R.id.navigationExtras);
        }else {
            Toast.makeText(this, "Error, todos los campos tienen que ir rellenos",
                    Toast.LENGTH_LONG).show();
        }
    }
}
