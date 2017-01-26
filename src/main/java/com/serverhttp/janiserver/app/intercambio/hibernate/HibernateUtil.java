package com.serverhttp.janiserver.app.intercambio.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author JAST
 */
public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory SESSION_FACTORY;

    public static SessionFactory getSessionFactory() {
        try {
            if (SESSION_FACTORY == null) {
                SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return SESSION_FACTORY;
    }
}
