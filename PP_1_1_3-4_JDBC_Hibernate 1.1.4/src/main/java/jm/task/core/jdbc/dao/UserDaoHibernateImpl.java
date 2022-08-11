package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String createTableCommand =
                "CREATE TABLE IF NOT EXISTS mysql.users " +
                        "(id INT NOT NULL AUTO_INCREMENT, " +
                        "name VARCHAR(20) NOT NULL, " +
                        "lastname VARCHAR(20) NOT NULL, " +
                        "age INT NOT NULL, " +
                        "PRIMARY KEY (id))";
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery(createTableCommand).executeUpdate();
        transaction.commit();
    }

    @Override
    public void dropUsersTable() {
        String dropTableCommand = "DROP TABLE IF EXISTS mysql.users";
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createSQLQuery(dropTableCommand).executeUpdate();
        transaction.commit();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(new User(name, lastName, age));
        transaction.commit();
        System.out.printf("User с именем - %s добавлен в базу данных\n", name);
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(session.load(User.class, id));
        } catch (EntityNotFoundException e) {

        }
        transaction.commit();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<User> list = session.createQuery("FROM User").list();
        transaction.commit();
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM User").executeUpdate();
        transaction.commit();
    }
}
