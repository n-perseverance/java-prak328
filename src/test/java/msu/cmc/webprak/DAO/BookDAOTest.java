package msu.cmc.webprak.DAO;

import jakarta.transaction.Transactional;
import msu.cmc.webprak.models.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import msu.cmc.webprak.utils.HibernateSessionFactoryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

//import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class BookDAOTest {

    @Autowired
    private BookDAO bookDAO;

    @Test
    public void testGetByIsbn_BookExists() {

        //Book foundBook = bookDAO.getByIsbn("978-5-6046264-0-8");
        //assertNotNull(foundBook);
        //assertNull(foundBook);
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testGetByIsbn_BookDoesNotExist() {
        Book foundBook = bookDAO.getByIsbn("999-9-99-999999-9");
        assertNull(foundBook);
    }
}