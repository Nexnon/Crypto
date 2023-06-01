package crypto.database;

import crypto.models.data.UserDB;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAO {
    public UserDB findByKey(String security_key) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(UserDB.class, security_key);
    }

    public UserDB findByEmailUsername(String username, String email) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from UserDB where email = :email1 and username = :username1").setParameter("email1", email).setParameter("username1", username).list();
        if(list.isEmpty()){
            return null;
        }
        return (UserDB) list.get(0);
    }

    public void saveUser(UserDB user) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }
}
