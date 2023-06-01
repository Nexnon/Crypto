package crypto.database;

import crypto.models.data.UserDB;
import crypto.models.data.WalletDB;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class WalletDAO {

    public List<WalletDB> findByUser(String secret_key) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from WalletDB where secret_key = :secret_key").setParameter("secret_key", secret_key).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public void saveWallet(WalletDB wallet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(wallet);
        tx1.commit();
        session.close();
    }
}
