package crypto.database;

import crypto.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO {
    public User findByKey(String security_key) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class, security_key);
    }

    public User findByEmailUsername(String username, String email) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from User where email = :email1 and username = :username1").setParameter("email1", email).setParameter("username1", username).list();
        if(list.isEmpty()){
            return null;
        }
        return (User) list.get(0);
    }

    public void saveUser(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void updateUser(User user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }
}
