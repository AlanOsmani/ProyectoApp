package com.example.psyquea.informacion;

public class Estado {

    String estado;
    String Fecha;
    String Hora;
    String chatU;

    public Estado(){

    }

    public Estado(String estado, String fecha, String hora, String chatU) {
        this.estado = estado;
        this.Fecha = fecha;
        this.Hora = hora;
        this.chatU = chatU;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getChatU() {
        return chatU;
    }

    public void setChatU(String chatU) {
        this.chatU = chatU;
    }
}
