package com.example.firebaseapp;

public class Cargar {
    private String nombre;
    private String precio;
    private String descripcion;
    private String url;

    public Cargar(){
    }
    public Cargar(String nombre,String url){
        if(nombre.trim().equals("")){
            nombre = "Sin nombre";
        }
        this.nombre = nombre;
        this.url = url;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
