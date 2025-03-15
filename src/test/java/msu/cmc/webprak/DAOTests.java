package msu.cmc.webprak;

import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.History;
import msu.cmc.webprak.java_entities.Reader;
import msu.cmc.webprak.DAO.ReaderDAO;
import msu.cmc.webprak.DAO.HistoryDAO;
import msu.cmc.webprak.configs.HibernateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = HibernateConfig.class)
public class DAOTests {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private ReaderDAO readerDAO;

    @Autowired
    private HistoryDAO historyDAO;


    @Test
    public void testBook() {
        Book book1 = new Book();
        Book book2 = bookDAO.getByIsbn("000");
        List<Book> books = bookDAO.getByTitle("Война и мир");
        book1.setIsbn("999");
        assertEquals("999", book1.getIsbn());
        assertNull(book2);

        Set<String> get = new HashSet<>();
        for (Book book : books) {
            get.add(book.getIsbn());
        }
        Set<String> ans = new HashSet<>();
        ans.add("978-5-389-08980-1");
        assertEquals(get, ans);

        books = bookDAO.getByAuthor("Лев Толстой");
        get = new HashSet<>();
        for (Book book : books) {
            get.add(book.getIsbn());
        }
        ans = new HashSet<>();
        ans.add("978-5-389-08980-1");
        assertEquals(get, ans);

        books = bookDAO.getByPublisher("Эксмо");
        get = new HashSet<>();
        for (Book book : books) {
            get.add(book.getIsbn());
        }
        ans = new HashSet<>();
        ans.add("978-5-04-116022-3");
        ans.add("978-5-6046529-2-0");
        assertEquals(get, ans);
    }

    @Test
    public void testCommon() {
        Book book = bookDAO.getById("999");
        assertNull(book);
        assertEquals(5, bookDAO.getAll().size());
        bookDAO.deleteById("000");
        assertNull(bookDAO.getById("000"));

        book = new Book();
        book.setIsbn("123-456");
        book.setName("Test");
        book.setAuthors(List.of("Test"));
        book.setPublisher("Test");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 1);
        Date year = calendar.getTime();
        book.setYear(year);
        bookDAO.save(book);

        Book book2 = bookDAO.getById(book.getIsbn());
        assertNotNull(book2);
        assertEquals("Test", book2.getName());
        book2.setName("New Test");
        bookDAO.update(book2);

        bookDAO.deleteById("123-456");
    }

    @Test
    public void testReader() {
        List<Reader> reader = readerDAO.getByName("Высочанская Алина Владимировна");
        assertEquals(1, reader.size());
        reader = readerDAO.getByPhoneNumber(999999999);
        assertEquals(0, reader.size());
    }

    @Test
    public void testHistory() {
        List<History> history = historyDAO.getByBookIsbn("978-5-6046264-0-8");
        assertEquals(2, history.size());
        history = historyDAO.getByReaderId(22);
        assertEquals(0, history.size());
        history = historyDAO.getByIssueDate(new Date(2025, 1, 1));
        assertEquals(0, history.size());
        history = historyDAO.getByReturnDate(new Date(2025, 1, 1));
        assertEquals(0, history.size());
        history = historyDAO.getByIssueDateRange(new Date(2025, 1, 1), new Date(2026, 1, 1));
        assertEquals(0, history.size());
        history = historyDAO.getByReturnDateRange(new Date(2025, 1, 1), new Date(2026, 1, 1));
        assertEquals(0, history.size());
    }
}
