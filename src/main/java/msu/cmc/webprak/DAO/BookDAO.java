package msu.cmc.webprak.DAO;

import msu.cmc.webprak.models.Book;

import java.util.List;

public interface BookDAO extends CommonDAO<Book, String>{
    Book getByIsbn(String isbn);

    List<Book> getByTitle(String title);

    List<Book> getByAuthor(String author);

    List<Book> getByPublisher(String publisher);
}