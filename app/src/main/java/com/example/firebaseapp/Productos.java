package com.example.firebaseapp;

public class Productos {
    private String nombre;
    private String precio;
    private String descripcion;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Productos(String nombre, String precio, String descripcion, String url) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}