package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.History;
import msu.cmc.webprak.java_entities.HistoryId;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class HistoryDAO extends CommonDAO<History, HistoryId> {

    public HistoryDAO() {
        super(History.class);
    }

    public List<History> getByBookIsbn(String isbn) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.id.isbn = :isbn", History.class);
        query.setParameter("isbn", isbn);
        return query.list();
    }

    public List<History> getByReaderId(Integer readerId) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.reader.id = :readerId", History.class);
        query.setParameter("readerId", readerId);
        return query.list();
    }

    public List<History> getByCopyId(Integer copyId) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.copy.copyId = :copyId", History.class);
        query.setParameter("copyId", copyId);
        return query.list();
    }

    public List<History> getByIssueDate(Date issueDate) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.issueDate = :issueDate", History.class);
        query.setParameter("issueDate", issueDate);
        return query.list();
    }

    public List<History> getByReturnDate(Date returnDate) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.returnDate = :returnDate", History.class);
        query.setParameter("returnDate", returnDate);
        return query.list();
    }

    public List<History> getByIssueDateRange(Date startDate, Date endDate) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.issueDate BETWEEN :startDate AND :endDate", History.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.list();
    }

    public List<History> getByReturnDateRange(Date startDate, Date endDate) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.returnDate BETWEEN :startDate AND :endDate", History.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.list();
    }

    public List<History> findByCopyAndDateRange(Integer copyId, Date startDate, Date endDate) {
        Session session = getCurrentSession();
        Query<History> query = session.createQuery(
                "FROM History h WHERE h.copy.copyId = :copyId " +
                        "AND h.issueDate BETWEEN :startDate AND :endDate " +
                        "ORDER BY h.issueDate DESC", History.class);

        query.setParameter("copyId", copyId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.list();
    }
}