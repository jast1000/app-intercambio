package com.serverhttp.janiserver.app.intercambio.service;

import com.serverhttp.janiserver.app.intercambio.model.Participante;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import java.util.List;

/**
 *
 * @author JAST
 */
public interface ParticipantesDAOInterface {

    public void init() throws Exception;

    public Usuario buscarUsuario(String usuario, String password) throws Exception;

    public Usuario buscarUsuario(String usuario) throws Exception;

    public void registrarUsuario(Usuario usuario) throws Exception;

    public List<Usuario> listarUsuariosPorEstado(Integer estado) throws Exception;

    public void eliminarUsuario(String usuario) throws Exception;

    public Participante obtenerPerfilParticipante(String usuario) throws Exception;

    public void guardarPerfilParticipante(Participante p) throws Exception;

    public void actualizarPerfilParticipante(Participante p) throws Exception;

}
