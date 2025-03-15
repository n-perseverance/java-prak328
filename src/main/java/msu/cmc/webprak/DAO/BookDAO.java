package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Book> getByPublisher(String publisher) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE publisher = :publisher", Book.class);
        query.setParameter("publisher", publisher);
        return query.list();
    }
}