package msu.cmc.webprak;

import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.DAO.ReaderDAO;
import msu.cmc.webprak.DAO.HistoryDAO;
import msu.cmc.webprak.configs.HibernateConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    //@Autowired
    //private ReaderDAO clientDAO;

    //@Autowired
    //private HistoryDAO orderDAO;


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
    }
}
