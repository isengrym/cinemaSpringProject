package ua.klieshchunov.spring.cinemaSpringProject.config.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import javax.imageio.spi.ServiceRegistry;
import java.util.Properties;

public class HibernateConfig {
    private static final SessionFactory factory;

    static {
        try {
            Configuration configuration = new Configuration();

            Properties settings = new Properties();
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
            settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
            settings.put(Environment.URL,"jdbc:mysql://localhost:3306/cinema");
            settings.put(Environment.USER, "root");
            settings.put(Environment.PASS, "h7fhk");
            configuration.setProperties(settings);

            configuration.addAnnotatedClass(User.class);

            factory = configuration.buildSessionFactory();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            throw new ExceptionInInitializerError(e);

        }
    }

    public static Session getSession() throws HibernateException {
        return factory.openSession();
    }
}
