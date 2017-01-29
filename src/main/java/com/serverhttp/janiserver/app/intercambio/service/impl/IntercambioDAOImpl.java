package com.serverhttp.janiserver.app.intercambio.service.impl;

import com.serverhttp.janiserver.app.intercambio.hibernate.HibernateUtil;
import com.serverhttp.janiserver.app.intercambio.model.Intercambio;
import com.serverhttp.janiserver.app.intercambio.model.PerfilUsuario;
import com.serverhttp.janiserver.app.intercambio.model.Usuario;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import com.serverhttp.janiserver.app.intercambio.service.IntercambioDAOInterface;

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
}
