package com.example.alberto.concesionario.BaseDeDatos.Coches;

import android.graphics.Bitmap;

public class Coche {

    /** ATRIBUTOS **/
    private int idCoche;
    private String marca;
    private String modelo;
    private int precio;
    private String descripcion;
    private Bitmap foto;
    private Boolean esNuevo;

    /**
     * Constructor de clase completo
     *
     * @param idCoche :int
     * @param marca :String
     * @param modelo :String
     * @param precio :int
     * @param descripcion :String
     * @param foto :Boolean
     * @param esNuevo :Bitmap
     */
    public Coche(int idCoche, String marca, String modelo, int precio, String descripcion, Bitmap foto, Boolean esNuevo) {
        this.idCoche = idCoche;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.foto = foto;
        this.esNuevo = esNuevo;
    }

    /**
     * Constructor de clase sin id
     *
     * @param marca :String
     * @param modelo :String
     * @param precio :int
     * @param descripcion :String
     * @param foto :Boolean
     * @param esNuevo :Bitmap
     */
    public Coche(String marca, String modelo, int precio, String descripcion, Bitmap foto, Boolean esNuevo) {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.foto = foto;
        this.esNuevo = esNuevo;
    }

    /**
     * Constructor de clase vacío
     */
    public Coche() {
    }

    public int getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(int idCoche) {
        this.idCoche = idCoche;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Boolean getEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(Boolean esNuevo) {
        this.esNuevo = esNuevo;
    }
}
