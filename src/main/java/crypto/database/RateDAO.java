package crypto.database;

import crypto.models.Operation;
import crypto.models.Rate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RateDAO {

    public static List<Rate> findByCurrency(String currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Rate where first_currency =: currency or second_currency =: currency").setParameter("currency", currency).list();
        if(list.isEmpty()){
            return null;
        }
        return list;
    }

    public static Rate findByCurrencies(String first_currency, String second_currency) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Rate where (first_currency =: first_currency and second_currency =: second_currency) or (first_currency =: second_currency and second_currency =: first_currency)").setParameter("first_currency", first_currency).setParameter("second_currency", second_currency).list();
        if(list.isEmpty()){
            return null;
        }

        return (Rate) list.get(0);
    }

    public static int getMaxID() {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("select max(id) from Rate").list();
        if(list.isEmpty()){
            return 0;
        }
        return (Integer) list.get(0);
    }

    public static void saveRate(Rate rate) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(rate);
        tx1.commit();
        session.close();
    }

    public static void updateRate(Rate rate) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(rate);
        tx1.commit();
        session.close();
    }

}
