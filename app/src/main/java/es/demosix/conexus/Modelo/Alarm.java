package es.demosix.conexus.Modelo;

import java.util.ArrayList;


public class Alarm {

    private String tipo;
    private String titulo;
    private String hora;
    private String latitud;
    private String longitud;
    private String bateria;
    private String dispositivo;

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getTipo() { return tipo; }

    public String getTitulo() {
        return titulo;
    }

    public String getHora() {
        return hora;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getBateria() {
        return bateria;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public Alarm(String tipo, String titulo, String hora, String latitud, String longitud, String bateria, String dispositivo) {

        this.tipo = tipo;
        this.titulo = titulo;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.bateria = bateria;
        this.dispositivo = dispositivo;
    }
}
