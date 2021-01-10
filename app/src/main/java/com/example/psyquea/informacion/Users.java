package com.example.psyquea.informacion;

public class Users {

    private String id;
    private String nombre;
    private String mail;
    private String tel;
    private String tipo;
    //private String cedula;
    private String pass;
    private String foto;
    private String estado;
    private String fecha;
    private String hora;


    private int solicitudes;
    private int nuevoMensaje;

    public Users(){

    }

    public Users(String id, String nombre, String mail, String tel, String tipo, String pass, String foto, String estado, String fecha, String hora, int solicitudes, int nuevoMensaje) {
        this.id = id;
        this.nombre = nombre;
        this.mail = mail;
        this.tel = tel;
        this.tipo = tipo;
        //this.cedula = cedula;
        this.pass = pass;
        this.foto = foto;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.solicitudes = solicitudes;
        this.nuevoMensaje = nuevoMensaje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /*public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }*/

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(int solicitudes) {
        this.solicitudes = solicitudes;
    }

    public int getNuevoMensaje() {
        return nuevoMensaje;
    }

    public void setNuevoMensaje(int nuevoMensaje) {
        this.nuevoMensaje = nuevoMensaje;
    }
}
