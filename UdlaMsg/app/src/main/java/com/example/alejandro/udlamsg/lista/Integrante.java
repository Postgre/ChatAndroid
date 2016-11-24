package com.example.alejandro.udlamsg.lista;

/**
 * Created by oswaldo on 20/11/2016.
 */
public class Integrante {
    private String[] nombres;
    private String[] estado;
    private String[] lmensaje;
    private String[] hora;
    private int[] codigo;
    private String[] imagenes;

    public String[] getNombres() {
        return nombres;
    }

    public void setNombres(String[] nombres) {
        this.nombres = nombres;
    }

    public String[] getEstado() {
        return estado;
    }

    public void setEstado(String[] estado) {
        this.estado = estado;
    }

    public String[] getLmensaje() {
        return lmensaje;
    }

    public void setLmensaje(String[] lmensaje) {
        this.lmensaje = lmensaje;
    }

    public String[] getHora() {
        return hora;
    }

    public void setHora(String[] hora) {
        this.hora = hora;
    }

    public int[] getCodigo() {
        return codigo;
    }

    public void setCodigo(int[] codigo) {
        this.codigo = codigo;
    }

    public String[] getImagenes() {
        return imagenes;
    }

    public void setImagenes(String[] imagenes) {
        this.imagenes = imagenes;
    }
}
