package com.serverhttp.janiserver.app.intercambio.model;

/**
 *
 * @author JAST
 */
public class PerfilUsuario {

    private String idParticipante;
    private String nombres;
    private Integer edad;
    private String sexo;
    private String grado;
    private String grupo;
    private String area;
    private String gustos;
    private String OpcionesIntercambio;
    private String usuario;

    public PerfilUsuario() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(String idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGustos() {
        return gustos;
    }

    public void setGustos(String gustos) {
        this.gustos = gustos;
    }

    public String getOpcionesIntercambio() {
        return OpcionesIntercambio;
    }

    public void setOpcionesIntercambio(String OpcionesIntercambio) {
        this.OpcionesIntercambio = OpcionesIntercambio;
    }
    
    @Override
    public String toString() {
        return "Participante{" + "idParticipante=" + idParticipante + ", nombres=" + nombres + ", edad=" + edad + ", sexo=" + sexo + ", grado=" + grado + ", grupo=" + grupo + ", area=" + area + ", gustos=" + gustos + ", usuario=" + usuario + '}';
    }
}
