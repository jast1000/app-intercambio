package com.serverhttp.janiserver.app.intercambio.model;

/**
 *
 * @author JAST
 */
public class EstadoAsistencia extends Intercambio {

    private Boolean confirmacion;

    public EstadoAsistencia() {
    }

    public Boolean getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }
}
