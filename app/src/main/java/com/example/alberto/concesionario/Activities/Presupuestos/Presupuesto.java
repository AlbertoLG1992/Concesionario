package com.example.alberto.concesionario.Activities.Presupuestos;

import com.example.alberto.concesionario.BaseDeDatos.Coches.Coche;
import com.example.alberto.concesionario.BaseDeDatos.Extras.Extra;

import java.io.Serializable;
import java.util.ArrayList;

public class Presupuesto implements Serializable {

    private String nombreCoche;
    private int precioCoche;
    private ArrayList<String> nombreExtras;
    private ArrayList<Integer> precioExtras;

    /**
     * Constructor de clase
     *
     * @param coche: Coche
     * @param listaExtras :ArrayList<Extra>
     */
    public Presupuesto(Coche coche, ArrayList<Extra> listaExtras) {
        this.nombreCoche = coche.getMarca() + " - " + coche.getModelo();
        this.precioCoche = coche.getPrecio();
        this.nombreExtras = new ArrayList<String>();
        this.precioExtras = new ArrayList<Integer>();
        for (int i = 0; i < listaExtras.size(); i++){
            this.nombreExtras.add(listaExtras.get(i).getNombre());
            this.precioExtras.add(listaExtras.get(i).getPrecio());
        }
    }

    /**
     * Devuelve un string con el total del presupuesto
     *
     * @return String
     */
    public String getPrecioTotalString(){
        return String.valueOf(this.generarPrecioPresupuesto()) + "€";
    }

    /**
     * Devuelve un string con el nombre del extra en la posición que entre por parametro
     *
     * @param posicion :int
     * @return String
     */
    public String getNombreExtraIndividual(int posicion){
        return this.nombreExtras.get(posicion);
    }

    /**
     * Devuelve un string con el precio del extra en la posición que entre por parametro
     *
     * @param posicion :int
     * @return String
     */
    public String getPrecioExtraIndividual(int posicion){
        return String.valueOf(this.precioExtras.get(posicion)) + "€";
    }

    /**
     * Devuelve un arrayList con la cabecera que tendra la tabla de la factura
     *
     * @return String
     */
    public ArrayList<String> cabeceraFactura(){
        ArrayList<String> listaOut = new ArrayList<String>();
        listaOut.add(this.getNombreCoche());
        listaOut.add(String.valueOf(this.precioCoche) + "€");
        return listaOut;
    }

    /**
     * Devuelve el número de columnas del presupuesto
     *
     * @return :int
     */
    public int numColumnasPresupuesto(){
        return 2;
    }

    /**
     * Devuelve el total del presupuesto
     *
     * @return int
     */
    public int generarPrecioPresupuesto(){
        int precioTotal = this.precioCoche;

        for (int i = 0; i < this.precioExtras.size(); i++){
            precioTotal += this.precioExtras.get(i);
        }

        return precioTotal;
    }

    /**
     * Devuelve la cantidad total de extras
     *
     * @return :int
     */
    public int numTotalExtras(){
        return this.nombreExtras.size();
    }

    public String getNombreCoche() {
        return nombreCoche;
    }

    public void setNombreCoche(String nombreCoche) {
        this.nombreCoche = nombreCoche;
    }

    public int getPrecioCoche() {
        return precioCoche;
    }

    public void setPrecioCoche(int precioCoche) {
        this.precioCoche = precioCoche;
    }

    public ArrayList<String> getNombreExtras() {
        return nombreExtras;
    }

    public void setNombreExtras(ArrayList<String> nombreExtras) {
        this.nombreExtras = nombreExtras;
    }

    public ArrayList<Integer> getPrecioExtras() {
        return precioExtras;
    }

    public void setPrecioExtras(ArrayList<Integer> precioExtras) {
        this.precioExtras = precioExtras;
    }
}
