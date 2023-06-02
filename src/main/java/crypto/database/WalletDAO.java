package crypto.database;

import crypto.models.Wallet;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class WalletDAO {

    public List<Wallet> findByUser(String secret_key) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Wallet where secret_key = :secret_key").setParameter("secret_key", secret_key).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public List<Wallet> findByCurrency(String currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Wallet where currency =: currency").setParameter("currency", currency).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public void saveWallet(Wallet wallet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(wallet);
        tx1.commit();
        session.close();
    }

    public void updateWallet(Wallet wallet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(wallet);
        tx1.commit();
        session.close();
    }
}
