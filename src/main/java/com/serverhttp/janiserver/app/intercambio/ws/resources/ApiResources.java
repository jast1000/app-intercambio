package com.serverhttp.janiserver.app.intercambio.ws.resources;

import com.serverhttp.janiserver.app.intercambio.model.Participante;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import com.serverhttp.janiserver.app.intercambio.service.impl.ParticipantesDAOImpl;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JAST
 */
@Path("api")
@RequestScoped
public class ApiResources {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiResources.class);

    private ParticipantesDAOImpl participantesDAO;

    @PostConstruct
    public void init() {
        LOGGER.info("meow");
        try {
            participantesDAO = new ParticipantesDAOImpl();
            participantesDAO.init();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @GET
    @Path("usuarios/participacion/{estado}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuarios(@PathParam("estado") Integer estado) {
        try {
            List<Usuario> us = participantesDAO.listarUsuariosPorEstado(estado);
            if (us != null && !us.isEmpty()) {
                return Response.ok(us.toArray(new Usuario[us.size()])).build();
            } else {
                return Response.noContent().build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarUsuario(Usuario u) {
        String pass = UUID.randomUUID().toString();
        pass = pass.substring(pass.length() - 12);
        LOGGER.info("Pass {}", pass);
        try {
            Usuario us = participantesDAO.buscarUsuario(u.getUsuario());
            if (us == null) {
                u.setPassword(pass);
                participantesDAO.registrarUsuario(u);
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity("{\"error\": \"Usuario ya existe\"}").build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("usuarios/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(@PathParam("usuario") String usuario) {
        try {
            participantesDAO.eliminarUsuario(usuario);
            return Response.ok().build();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("usuarios/identificacion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response identificarUsuario(Usuario u) {
        try {
            Usuario us = participantesDAO.buscarUsuario(u.getUsuario(), u.getPassword());
            if (us == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("\"error\": \"Usuario no existe\"").build();
            } else {
                us.setPassword(null);
                return Response.ok(us).build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("participantes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarParticipante(Participante p) {
        try {
            Usuario us = participantesDAO.buscarUsuario(p.getUsuario());
            if (us != null) {
                participantesDAO.guardarPerfilParticipante(p);
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity("{\"error\": \"Usuario no existe\"}").build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("participantes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificarPerfilParticipante(Participante p) {
        try {
            Usuario us = participantesDAO.buscarUsuario(p.getUsuario());
            if (us != null) {
                participantesDAO.actualizarPerfilParticipante(p);
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity("{\"error\": \"Usuario no existe\"}").build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }

    }

    @GET
    @Path("participantes/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtenerPerfilParticipante(@PathParam("usuario") String usuario) {
        try {
            Participante p = participantesDAO.obtenerPerfilParticipante(usuario);
            if (p == null) {
                return Response.status(Response.Status.NO_CONTENT)
                        .entity("\"error\": \"Perfil de usuario aún no existe\"").build();
            } else {
                return Response.ok(p).build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }
}
