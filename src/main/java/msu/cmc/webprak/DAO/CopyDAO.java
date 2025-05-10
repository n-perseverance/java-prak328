package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.Copy;
import msu.cmc.webprak.java_entities.History;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CopyDAO extends CommonDAO<Copy, Integer> {

    public CopyDAO() {
        super(Copy.class);
    }

    public Copy getById(Integer id) {
        return getCurrentSession().get(Copy.class, id);
    }

    public List<History> historyById(Integer copyId) {
        Session session = getCurrentSession();

        String hql = "FROM History o WHERE o.copy.copyId = :copyId";

        Query<History> query = session.createQuery(hql, History.class);

        query.setParameter("copyId",copyId);
        return query.list();
    }

}