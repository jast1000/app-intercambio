package com.serverhttp.janiserver.app.intercambio.ws.resources;

import com.serverhttp.janiserver.app.intercambio.model.Intercambio;
import com.serverhttp.janiserver.app.intercambio.model.PerfilUsuario;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import com.serverhttp.janiserver.app.intercambio.service.impl.IntercambioDAOImpl;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    private IntercambioDAOImpl intercambioDAO;

    @PostConstruct
    public void init() {
        try {
            intercambioDAO = new IntercambioDAOImpl();
            intercambioDAO.init();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @POST
    @Path("usuarios/identificacion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response identificarUsuario(Usuario u) {
        try {
            Usuario us = intercambioDAO.buscarUsuario(u.getUsuario(), u.getPassword());
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
    @Path("usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarUsuario(Usuario u) {
        String pass = UUID.randomUUID().toString();
        pass = pass.substring(pass.length() - 12);
        LOGGER.info("Pass {}", pass);
        try {
            Usuario us = intercambioDAO.buscarUsuario(u.getUsuario());
            if (us == null) {
                u.setPassword(pass);
                intercambioDAO.registrarUsuario(u);
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
    
    @GET
    @Path("perfiles/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response obtenerPerfilParticipante(@PathParam("usuario") String usuario) {
        try {
            PerfilUsuario pu = intercambioDAO.obtenerPerfilParticipante(usuario);
            if (pu == null || pu.getNombres() == null || pu.getNombres().isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.ok(pu).build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }
    
    @POST
    @Path("perfiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarPerfil(PerfilUsuario p) {
        try {
            Usuario us = intercambioDAO.buscarUsuario(p.getUsuario());
            if (us != null) {
                intercambioDAO.guardarPerfilParticipante(p);
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

    @POST
    @Path("intercambios")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response guardarIntercambio(Intercambio i) {
        try {
            List<Intercambio> intercambios = intercambioDAO.listarIntercambios();
            if (intercambios != null && !intercambios.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Ya existe un intercambio creado\"}")
                        .build();
            } else {
                intercambioDAO.guardarIntercambio(i);
                return Response.ok().build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("intercambios")
    public Response listarIntercambios() {
        try {
            List<Intercambio> intercambios = intercambioDAO.listarIntercambios();
            if (intercambios != null && !intercambios.isEmpty()) {
                return Response.ok(intercambios.toArray(new Intercambio[intercambios.size()])).build();
            } else {
                return Response.noContent().build();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return Response.serverError().entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        }
    }
}
