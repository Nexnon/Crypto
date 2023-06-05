package crypto.database;

import crypto.models.Wallet;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class WalletDAO {

    public static List<Wallet> findByUser(String secret_key) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Wallet where secret_key = :secret_key").setParameter("secret_key", secret_key).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public static List<Wallet> findByCurrency(String currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Wallet where currency =: currency").setParameter("currency", currency).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public static int getMaxID() {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("select max(id) from Wallet").list();
        if(list.isEmpty()){
            return 0;
        }
        return (Integer) list.get(0);
    }

    public static void saveWallet(Wallet wallet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(wallet);
        tx1.commit();
        session.close();
    }

    public static void updateWallet(Wallet wallet) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(wallet);
        tx1.commit();
        session.close();
    }
}
