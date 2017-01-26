package com.serverhttp.janiserver.app.intercambio.service.impl;

import com.serverhttp.janiserver.app.intercambio.hibernate.HibernateUtil;
import com.serverhttp.janiserver.app.intercambio.model.Participante;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import com.serverhttp.janiserver.app.intercambio.service.ParticipantesDAOInterface;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

/**
 *
 * @author JAST
 */
public class ParticipantesDAOImpl implements ParticipantesDAOInterface {

    private SessionFactory sf;

    @Override
    public void init() throws Exception {
        sf = HibernateUtil.getSessionFactory();
    }

    @Override
    public Usuario buscarUsuario(String usuario, String password) throws Exception {
        Usuario u;
        String sql = "select \"IdUsuario\" as usuario, \"Password\" as password, \"TipoUsuario\" as \"tipoUsuario\" from \"Usuarios\""
                + " where \"IdUsuario\" = ? and \"Password\" = ?";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        u = (Usuario) s.createSQLQuery(sql)
                .setString(0, usuario)
                .setString(1, password)
                .setResultTransformer(Transformers.aliasToBean(Usuario.class))
                .uniqueResult();
        s.getTransaction().commit();
        return u;
    }

    @Override
    public Usuario buscarUsuario(String usuario) throws Exception {
        Usuario u;
        String sql = "select \"IdUsuario\" as usuario, \"Password\" as password, \"TipoUsuario\" as \"tipoUsuario\" from \"Usuarios\""
                + " where \"IdUsuario\" = ?";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        u = (Usuario) s.createSQLQuery(sql)
                .setString(0, usuario)
                .setResultTransformer(Transformers.aliasToBean(Usuario.class))
                .uniqueResult();
        s.getTransaction().commit();
        return u;
    }

    @Override
    public void registrarUsuario(Usuario u) throws Exception {
        String sql = "insert into \"Usuarios\"(\"IdUsuario\", \"Password\", \"TipoUsuario\") values(?, ?, ?)";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setString(0, u.getUsuario())
                .setString(1, u.getPassword())
                .setInteger(2, u.getTipoUsuario())
                .executeUpdate();
        s.getTransaction().commit();
    }

    @Override
    public List<Usuario> listarUsuariosPorEstado(Integer estado) throws Exception {
        List<Usuario> us;
        String sql = "select u.\"IdUsuario\" as usuario, u.\"TipoUsuario\" from \"Usuarios\" as u"
                + " left join \"Participantes\" as a using(\"IdUsuario\")"
                + " left join \"Asistencia\" as asi using(\"IdPart\")";
        String where = "";
        if (1 == estado) {
            where += " where asi.\"Afirmacion\" is null";
        } else if (2 == estado) {
            where += " where asi.\"Afirmacion\" = true";
        } else if (3 == estado) {
            where += " where asi.\"Afirmacion\" = false";
        }
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        us = (List<Usuario>) s.createSQLQuery(sql + where)
                .setResultTransformer(Transformers.aliasToBean(Usuario.class))
                .list();
        s.getTransaction().commit();
        return us;
    }

    @Override
    public Participante obtenerPerfilParticipante(String usuario) throws Exception {
        String sql = "SELECT \"IdPart\" as \"idParticipante\", \"Nombre\" as nombres, \"Edad\" as edad, \"Sexo\" as sexo, \"Grado\" as grado, "
                + " \"Grupo\" as grupo, \"Area\" as area, \"Gustos\" as gustos, \"IdUsuario\" as usuario FROM \"Participantes\" "
                + " where \"IdUsuario\" = ?";
        Participante p;
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        p = (Participante) s.createSQLQuery(sql)
                .setString(0, usuario)
                .setResultTransformer(Transformers.aliasToBean(Participante.class))
                .uniqueResult();
        s.getTransaction().commit();
        return p;
    }

    @Override
    public void guardarPerfilParticipante(Participante p) throws Exception {
        String insertParticipantes = "insert into \"Participantes\"(\"IdPart\", \"Nombre\", \"Edad\", "
                + "\"Sexo\", \"Grado\", \"Grupo\", \"Area\", \"Gustos\", \"IdUsuario\") values(?, ?, ?, ?, ?, ?, ?,?, ?)";
        String insertAsistencia = "insert into \"Asistencia\"(\"IdPart\", \"Afirmacion\") values(?, NULL);";
        String idParticipante = UUID.randomUUID().toString();
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        s.createSQLQuery(insertParticipantes)
                .setString(0, idParticipante)
                .setString(1, p.getNombres())
                .setInteger(2, p.getEdad())
                .setString(3, p.getSexo())
                .setString(4, p.getGrado())
                .setString(5, p.getGrupo())
                .setString(6, p.getArea())
                .setString(7, p.getGustos())
                .setString(8, p.getUsuario())
                .executeUpdate();
        s.createSQLQuery(insertAsistencia)
                .setString(0, idParticipante)
                .executeUpdate();

        s.getTransaction().commit();
    }

    @Override
    public void eliminarUsuario(String usuario) throws Exception {
        String sqlPart = "select \"IdPart\" from \"Participantes\" where \"IdUsuario\" = ?";
        String sqlDelSugerencias = "delete from \"Sugerencias\" where \"IdPart\" = ?";
        String sqlDelAsistencia = "delete from \"Asistencia\" where \"IdPart\" = ?";
        String sqlDelParticipante = "delete from \"Participantes\" where \"IdPart\" = ?";
        String sqlDelUsuarios = "delete from \"Usuarios\" where \"IdUsuario\" = ?";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        String idParticipante = (String) s.createSQLQuery(sqlPart)
                .setString(0, usuario)
                .uniqueResult();
        if (idParticipante != null) {
            s.createSQLQuery(sqlDelSugerencias)
                    .setString(0, idParticipante)
                    .executeUpdate();
            s.createSQLQuery(sqlDelAsistencia)
                    .setString(0, idParticipante)
                    .executeUpdate();
            s.createSQLQuery(sqlDelParticipante)
                    .setString(0, idParticipante)
                    .executeUpdate();
        }
        s.createSQLQuery(sqlDelUsuarios)
                .setString(0, usuario)
                .executeUpdate();
        s.getTransaction().commit();
    }

    @Override
    public void actualizarPerfilParticipante(Participante p) throws Exception {
        String sql = "update \"Participantes\" set \"Nombre\" = ?, \"Edad\" = ?, \"Sexo\" = ?, "
                + "\"Grado\" = ?, \"Grupo\" = ?, \"Area\" = ?, \"Gustos\" = ? where \"IdUsuario\" = ?;";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setString(0, p.getNombres())
                .setInteger(1, p.getEdad())
                .setString(2, p.getSexo())
                .setString(3, p.getGrado())
                .setString(4, p.getGrupo())
                .setString(5, p.getArea())
                .setString(6, p.getGustos())
                .setString(7, p.getUsuario())
                .executeUpdate();
        s.getTransaction().commit();
    }
}
