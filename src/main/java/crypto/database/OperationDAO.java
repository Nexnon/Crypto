package crypto.database;

import crypto.models.Operation;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class OperationDAO {

    public List<Operation> findByDates(Date dateFrom, Date dateTo) {
        List list = HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Operation op where op.date between :dateFrom and :dateTo").setParameter("dateFrom", dateFrom).setParameter("dateTo", dateTo).list();
        return list;
    }

    public void saveOperation(Operation operation) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(operation);
        tx1.commit();
        session.close();
    }
}
