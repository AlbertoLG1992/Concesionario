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
     * Genera un string con el presupuesto
     *
     * @return String
     */
    public String generarPresupuesto(){
        String presupuestoTotal = this.nombreCoche + " ----> " + this.precioCoche + "€\n";

        for (int i = 0; i < this.nombreExtras.size(); i++){
            presupuestoTotal += this.nombreExtras.get(i) + " ----> " + this.precioExtras.get(i) + "€\n";
        }

        presupuestoTotal += "\n\n";
        presupuestoTotal += "Total ----> " + this.generarPrecioPresupuesto() + "€";

        return presupuestoTotal;
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
