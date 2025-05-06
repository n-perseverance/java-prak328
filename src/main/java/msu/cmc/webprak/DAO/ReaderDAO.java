package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Reader;
import msu.cmc.webprak.java_entities.History;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

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

    public List<Reader> searchReaders(String name, String phone) {
        Session session = getCurrentSession();
        StringBuilder hql = new StringBuilder("FROM Reader WHERE 1=1");

        if (name != null && !name.isEmpty()) {
            hql.append(" AND lower(name) LIKE lower(:name)");
        }
        if (phone != null && !phone.isEmpty()) {
            hql.append(" AND cast(phoneNumber as string) LIKE :phone");
        }

        Query<Reader> query = session.createQuery(hql.toString(), Reader.class);

        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (phone != null && !phone.isEmpty()) {
            query.setParameter("phone", "%" + phone + "%");
        }

        return query.list();
    }

    public List<History> historyById(Integer readerId) {
        Session session = getCurrentSession();

        String hql = "FROM History o WHERE o.reader.id = :readerId";

        Query<History> query = session.createQuery(hql, History.class);

        query.setParameter("readerId",readerId);
        return query.list();
    }
}