package com.example.alberto.concesionario.BaseDeDatos.Extras;

public class Extra {

    /** ATRIBUTOS **/
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;

    /**
     * Constructor de clase completo
     * @param id :int
     * @param nombre :String
     * @param descripcion :String
     * @param precio :int
     */
    public Extra(int id, String nombre, String descripcion, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    /**
     * Constructor de clase completo sin id
     *
     * @param nombre :String
     * @param descripcion :String
     * @param precio :int
     */
    public Extra(String nombre, String descripcion, int precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    /**
     * Constructor de clase vacío
     */
    public Extra() {
    }

    /**
     * Constructor de clase con un objeto de su misma clase
     *
     * @param extra :Extra
     */
    public Extra(Extra extra){
        this.id = extra.getId();
        this.nombre = extra.getNombre();
        this.descripcion = extra.getDescripcion();
        this.precio = extra.getPrecio();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
