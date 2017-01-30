package com.serverhttp.janiserver.app.intercambio.model;

import java.util.Date;

/**
 *
 * @author JAST
 */
public class Intercambio {

    private Integer idRegla;
    private String lugar;
    private Date fecha;
    private Double monto;
    private Short estado;
    
    public Intercambio() {
    }

    public Integer getIdRegla() {
        return idRegla;
    }

    public void setIdRegla(Integer idRegla) {
        this.idRegla = idRegla;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Intercambio{" + "idRegla=" + idRegla + ", lugar=" + lugar + ", fecha=" + fecha + ", monto=" + monto + '}';
    }
}
