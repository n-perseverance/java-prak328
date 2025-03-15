package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Reader;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class ReaderDAO extends CommonDAO<Reader, Integer> {

    public ReaderDAO() {
        super(Reader.class);
    }

    public List<Reader> getByName(String name) {
        Session session = getCurrentSession();
        Query<Reader> query = session.createQuery("FROM Reader WHERE name = :name", Reader.class);
        query.setParameter("name", name);
        return query.list();
    }

    public List<Reader> getByPhoneNumber(long phoneNumber) {
        Session session = getCurrentSession();
        Query<Reader> query = session.createQuery("FROM Reader WHERE phoneNumber = :phoneNumber", Reader.class);
        query.setParameter("phoneNumber", phoneNumber);
        return query.list();
    }
}