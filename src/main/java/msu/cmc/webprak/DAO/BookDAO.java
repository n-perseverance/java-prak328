package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.Copy;
import msu.cmc.webprak.java_entities.History;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class BookDAO extends CommonDAO<Book, String> {

    public BookDAO() {
        super(Book.class);
    }

    public Book getByIsbn(String isbn) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        return query.uniqueResult();
    }

    public List<Book> getByTitle(String title) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE name = :title", Book.class);
        query.setParameter("title", title);
        return query.list();
    }

    public List<Book> getByAuthor(String author) {
        Session session = getCurrentSession();
        Query<Book> query = session.createNativeQuery(
                "SELECT * FROM books WHERE :author = ANY(authors)",
                Book.class
        );        query.setParameter("author", author);
        return query.list();
    }

    public List<Copy> copyById(String bookId) {
        Session session = getCurrentSession();

        String hql = "FROM Copy o WHERE o.book.isbn = :bookId";

        Query<Copy> query = session.createQuery(hql, Copy.class);

        query.setParameter("bookId",bookId);
        return query.list();
    }

    public List<Copy> copyByCopyId(Integer copyId) {
        Session session = getCurrentSession();

        String hql = "FROM Copy o WHERE o.copyId = :copyId";

        Query<Copy> query = session.createQuery(hql, Copy.class);

        query.setParameter("copyId",copyId);
        return query.list();
    }

    public List<Book> getByPublisher(String publisher) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE publisher = :publisher", Book.class);
        query.setParameter("publisher", publisher);
        return query.list();
    }

    public List<Book> filterBooks(String isbn, String title, String author, String publisher) {
        Session session = getCurrentSession();
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE TRUE");
        Map<String, Object> params = new HashMap<>();

        if (isbn != null && !isbn.isEmpty()) {
            sql.append(" AND isbn ILIKE :isbn");
            params.put("isbn", "%" + isbn + "%");
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND name ILIKE :title");
            params.put("title", "%" + title + "%");
        }
        if (author != null && !author.isEmpty()) {
            sql.append(" AND EXISTS (SELECT 1 FROM unnest(authors) a WHERE a ILIKE :author)");
            params.put("author", "%" + author + "%");
        }
        if (publisher != null && !publisher.isEmpty()) {
            sql.append(" AND publisher ILIKE :publisher");
            params.put("publisher", "%" + publisher + "%");
        }

        NativeQuery<Book> query = session.createNativeQuery(sql.toString(), Book.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }

}