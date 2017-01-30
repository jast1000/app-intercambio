package com.serverhttp.janiserver.app.intercambio.service;

import com.serverhttp.janiserver.app.intercambio.model.EstadoAsistencia;
import com.serverhttp.janiserver.app.intercambio.model.Intercambio;
import com.serverhttp.janiserver.app.intercambio.model.PerfilUsuario;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import java.util.List;

/**
 *
 * @author JAST
 */
public interface IntercambioDAOInterface {

    public void init() throws Exception;

    public Usuario buscarUsuario(String usuario, String password) throws Exception;

    public Usuario buscarUsuario(String usuario) throws Exception;

    public void registrarUsuario(Usuario usuario) throws Exception;

    public void guardarPerfilParticipante(PerfilUsuario p) throws Exception;

    public PerfilUsuario obtenerPerfilParticipante(String usuario) throws Exception;

    public List<Intercambio> listarIntercambios() throws Exception;

    public void guardarIntercambio(Intercambio i) throws Exception;

    public void generarSorteo() throws Exception;
    
    public PerfilUsuario consultarPerfilPareja(String idUsuario) throws Exception;
    
    public EstadoAsistencia consultarEstadoAsistencia(String idUsuario) throws Exception;
    
    public void confirmarAsistencia(String idParticipante, Boolean asistencia) throws Exception;
}
