package com.serverhttp.janiserver.app.intercambio.service.impl;

import com.serverhttp.janiserver.app.intercambio.hibernate.HibernateUtil;
import com.serverhttp.janiserver.app.intercambio.model.EstadoAsistencia;
import com.serverhttp.janiserver.app.intercambio.model.Intercambio;
import com.serverhttp.janiserver.app.intercambio.model.PerfilUsuario;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import com.serverhttp.janiserver.app.intercambio.service.IntercambioDAOInterface;
import org.hibernate.SQLQuery;

/**
 *
 * @author JAST
 */
public class IntercambioDAOImpl implements IntercambioDAOInterface {

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
    public void guardarPerfilParticipante(PerfilUsuario p) throws Exception {
        String sql = "update \"Participantes\" set \"Nombre\" = ?, \"Edad\" = ?, \"Sexo\" = ?, "
                + "\"Grado\" = ?, \"Grupo\" = ?, \"Area\" = ?, \"Gustos\" = ?, \"OpcionesIntercambio\" = ? where \"IdUsuario\" = ?;";
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
                .setString(7, p.getOpcionesIntercambio())
                .setString(8, p.getUsuario())
                .executeUpdate();
        s.getTransaction().commit();
    }

    @Override
    public PerfilUsuario obtenerPerfilParticipante(String usuario) throws Exception {
        String sql = "SELECT \"IdPart\" as \"idParticipante\", \"Nombre\" as nombres, \"Edad\" as edad, \"Sexo\" as sexo, \"Grado\" as grado, "
                + " \"Grupo\" as grupo, \"Area\" as area, \"Gustos\" as gustos, \"IdUsuario\" as usuario, "
                + " \"OpcionesIntercambio\" as \"opcionesIntercambio\" FROM \"Participantes\" where \"IdUsuario\" = ?";
        PerfilUsuario pu;
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        pu = (PerfilUsuario) s.createSQLQuery(sql)
                .setString(0, usuario)
                .setResultTransformer(Transformers.aliasToBean(PerfilUsuario.class))
                .uniqueResult();
        s.getTransaction().commit();
        return pu;
    }

    @Override
    public List<Intercambio> listarIntercambios() throws Exception {
        List<Intercambio> intercambios;
        String sqlCant = "select cast(count(*) as int) from \"Sorteo\"";
        String sql = "select \"IdRegla\" as \"idRegla\", \"Lugar\" as lugar, \"Fecha\" as fecha, \"Monto\" as monto from \"Reglas\"";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        Integer cantidad = (Integer) s.createSQLQuery(sqlCant).uniqueResult();
        intercambios = (List<Intercambio>) s.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(Intercambio.class))
                .list();
        for (Intercambio i : intercambios) {
            i.setEstado(cantidad == 0 ? new Short("1") : new Short("2"));
        }
        s.getTransaction().commit();
        return intercambios;
    }

    @Override
    public void guardarIntercambio(Intercambio i) throws Exception {
        String sql = "insert into \"Reglas\"(\"Lugar\", \"Fecha\", \"Monto\")"
                + " values(?, ?, ?);";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        s.createSQLQuery(sql)
                .setString(0, i.getLugar())
                .setTimestamp(1, i.getFecha())
                .setDouble(2, i.getMonto())
                .executeUpdate();
        s.getTransaction().commit();
    }

    @Override
    public void generarSorteo() throws Exception {
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        String sqlCant = "select cast(count(*) as int) from \"Sorteo\"";
        String sqlParts = "select p.\"IdPart\" from \"Participantes\" as p inner join \"Asistencia\" using(\"IdPart\") "
                + " where \"Afirmacion\" = true";
        String sqlSorteo = "insert into \"Sorteo\"(\"IdPart1\", \"IdPartInter\") values(?, ?)";
        Integer cantidad = (Integer) s.createSQLQuery(sqlCant).uniqueResult();
        if (cantidad > 0) {
            throw new Exception("Ya existe un sorteo");
        }
        List<String> participantes = (List<String>) s.createSQLQuery(sqlParts)
                .list();
        int iteraciones = participantes.size() * 5;
        for (int i = 1; i <= iteraciones; i++) {
            int p = (int) (Math.random() * participantes.size());
            participantes.add(participantes.remove(p));
        }
        int i = 0;
        SQLQuery query;
        while (i < participantes.size()) {
            query = s.createSQLQuery(sqlSorteo);
            if (i == participantes.size() - 1) {
                query.setString(0, participantes.get(i))
                        .setString(1, participantes.get(0));
            } else {
                query.setString(0, participantes.get(i))
                        .setString(1, participantes.get(i + 1));
            }
            query.executeUpdate();
            i++;
        }
        s.getTransaction().commit();
    }

    @Override
    public PerfilUsuario consultarPerfilPareja(String idUsuario) throws Exception {
        PerfilUsuario pu;
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        String sql = "select p.\"IdPart\" as \"idParticipante\", p.\"Nombre\" as nombres, p.\"Edad\" as edad, p.\"Sexo\" as sexo, p.\"Grado\" as grado, \n"
                + "p.\"Grupo\" as grupo, p.\"Area\" as area, p.\"Gustos\" as gustos, p.\"OpcionesIntercambio\" as \"opcionesIntercambio\" from \"Sorteo\" as s\n"
                + "inner join \"Participantes\" as p on s.\"IdPartInter\" = p.\"IdPart\"\n"
                + "inner join \"Participantes\" as p1 on p1.\"IdPart\" = s.\"IdPart1\"\n"
                + "inner join \"Usuarios\" as u on u.\"IdUsuario\" = p1.\"IdUsuario\"\n"
                + "where u.\"IdUsuario\" = ?";
        pu = (PerfilUsuario) s.createSQLQuery(sql)
                .setString(0, idUsuario)
                .setResultTransformer(Transformers.aliasToBean(PerfilUsuario.class))
                .uniqueResult();
        s.getTransaction().commit();
        return pu;
    }

    @Override
    public EstadoAsistencia consultarEstadoAsistencia(String idUsuario) throws Exception {
        EstadoAsistencia asis;
        String sql = "select r.\"Lugar\" as lugar, r.\"Fecha\" as fecha, r.\"Monto\" as monto, a.\"Afirmacion\" as confirmacion from \"Asistencia\" as a \n"
                + "inner join \"Participantes\" as p on a.\"IdPart\" = p.\"IdPart\"\n"
                + "inner join \"Usuarios\" as u on u.\"IdUsuario\" = p.\"IdUsuario\"\n"
                + "inner join \"Reglas\" as r on true = true\n"
                + "where u.\"IdUsuario\" = ?";
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        asis = (EstadoAsistencia) s.createSQLQuery(sql)
                .setString(0, idUsuario)
                .setResultTransformer(Transformers.aliasToBean(EstadoAsistencia.class))
                .uniqueResult();
        s.getTransaction().commit();
        return asis;
    }

    @Override
    public void confirmarAsistencia(String idParticipante, Boolean asistencia) throws Exception {
        Session s = sf.getCurrentSession();
        s.beginTransaction();
        String sql = "update \"Asistencia\" set \"Afirmacion\" = ? where \"IdPart\" = ?";
        s.createSQLQuery(sql)
                .setBoolean(0, asistencia)
                .setString(1, idParticipante)
                .executeUpdate();
        s.getTransaction().commit();
    }
}
