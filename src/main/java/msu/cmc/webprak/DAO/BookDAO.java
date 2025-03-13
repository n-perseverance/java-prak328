package msu.cmc.webprak.DAO;

import msu.cmc.webprak.java_entities.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDAO extends CommonDAO<Book, String> {

    public BookDAO() {
        super(Book.class);
    }

    public Book getByIsbn(String isbn) {
        return getById(isbn);
    }

    public List<Book> getByTitle(String title) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE name = :title", Book.class);
        query.setParameter("title", title);
        return query.list();
    }

    public List<Book> getByAuthor(String author) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE :author IN elements(authors)", Book.class);
        query.setParameter("author", author);
        return query.list();
    }

    public List<Book> getByPublisher(String publisher) {
        Session session = getCurrentSession();
        Query<Book> query = session.createQuery("FROM Book WHERE publisher = :publisher", Book.class);
        query.setParameter("publisher", publisher);
        return query.list();
    }
}