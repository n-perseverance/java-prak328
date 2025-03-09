package msu.cmc.webprak.DAO.impl;

import jakarta.persistence.TypedQuery;
import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.models.Book;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import msu.cmc.webprak.utils.HibernateSessionFactoryUtil;

import java.util.List;

@Repository
public class BookDAOImpl extends CommonDAOImpl<Book, String> implements BookDAO {

    public BookDAOImpl() {
        super();
        setEntityClass(Book.class);
    }

    @Override
    public Book getByIsbn(String isbn) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, isbn);
        }
    }

    @Override
    public List<Book> getByTitle(String title) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            TypedQuery<Book> query = session.createQuery("SELECT b FROM Book b WHERE b.name LIKE :title", Book.class);
            query.setParameter("title", "%" + title + "%");
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    @Override
    public List<Book> getByAuthor(String author) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            TypedQuery<Book> query = session.createQuery("SELECT b FROM Book b WHERE :author IN elements(b.authors)", Book.class);
            query.setParameter("author", author);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }

    @Override
    public List<Book> getByPublisher(String publisher) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            TypedQuery<Book> query = session.createQuery("SELECT b FROM Book b WHERE b.publisher = :publisher", Book.class);
            query.setParameter("publisher", publisher);
            return query.getResultList().isEmpty() ? null : query.getResultList();
        }
    }
}