package crypto.database;

import crypto.models.data.RateDB;
import crypto.models.data.WalletDB;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RateDAO {

    public List<RateDB> findByCurrency(String currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from RateDB where first_currency =: currency or second_currency =: currency").setParameter("currency", currency).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public RateDB findByCurrencies(String first_currency, String second_currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from RateDB where (first_currency =: first_currency and second_currency =: second_currency) or (first_currency =: second_currency and second_currency =: first_currency)").setParameter("first_currency", first_currency).setParameter("second_currency", second_currency).list();
        if(list.isEmpty()){
            return null;
        }

        return (RateDB) list.get(0);
    }

    public void saveRate(RateDB rate) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(rate);
        tx1.commit();
        session.close();
    }

    public void updateRate(RateDB rate) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(rate);
        tx1.commit();
        session.close();
    }

}
