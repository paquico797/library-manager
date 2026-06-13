package com.franRR.librarymanager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactory = buildSessionFactory();

    public static SessionFactory buildSessionFactory() {
        try{
            logger.info("Inicializando la configuración de Hibernate");
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Error al inicializar Hibernate", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown(){
        if (sessionFactory !=null || !sessionFactory.isClosed()){
            sessionFactory.close();
            logger.info("SessionFactory cerrada correctamente");
        }
    }
    
}
