package msu.cmc.webprak.Controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.DAO.CopyDAO;
import msu.cmc.webprak.DAO.HistoryDAO;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.Copy;
import msu.cmc.webprak.java_entities.History;
import msu.cmc.webprak.java_entities.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private CopyDAO copyDAO;

    @Autowired
    private HistoryDAO historyDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

        binder.registerCustomEditor(List.class, "authors", new CustomCollectionEditor(List.class) {
            @Override
            protected Object convertElement(Object element) {
                return element == null ? null : element.toString().trim();
            }
        });
    }

    @GetMapping("/books")
    public String listBooks(Model model,
                            @RequestParam(value = "isbn", required = false) String isbn,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "author", required = false) String author,
                            @RequestParam(value = "publisher", required = false) String publisher) {

        List<Book> books = bookDAO.filterBooks(isbn,name,author,publisher);
        model.addAttribute("books", books);
        model.addAttribute("isbn", isbn);
        model.addAttribute("name",name);
        model.addAttribute("author",author);
        model.addAttribute("publisher",publisher);
        return "books";
    }

    @GetMapping("/books/{bookId}")
    public String showBook(@PathVariable String bookId, Model model) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            return "error";
        }
        model.addAttribute("book", book);
        model.addAttribute("copy", bookDAO.copyById(bookId));
        return "bookId";
    }

    @GetMapping("books/add")
    public String addBookpage(Model model) {
        model.addAttribute("newBook", new Book());
        return "addBook";
    }

    @GetMapping("/books/{bookId}/delete")
    public String deleteBook(@PathVariable String bookId, Model model) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            return "error";
        }
        bookDAO.delete(book);
        return "redirect:/books";
    }

    @GetMapping("/copies/{copyId}/delete")
    public String deleteCopy(@PathVariable Integer copyId, Model model) {
        Copy copy = copyDAO.getById(copyId);
        if (copy == null) {
            return "error";
        }
        String bookId = copy.getBook().getId();
        copyDAO.delete(copy);
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/copies/{copyId}/edit")
    public String showCopyDetails(
            @PathVariable Integer copyId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        Copy copy = copyDAO.getById(copyId);
        if (copy == null) {
            return "error";
        }

        List<History> history;
        if (startDate == null && endDate == null) {
            history = copyDAO.historyById(copyId);
        }
        else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date start = startDate != null ? dateFormat.parse(startDate) : null;
                Date end = endDate != null ? dateFormat.parse(endDate) : null;

                history = historyDAO.findByCopyAndDateRange(copyId, start, end);
            } catch (ParseException e) {
                history = copyDAO.historyById(copyId);
                model.addAttribute("errorMessage", "Неверный формат даты. Используйте ГГГГ-ММ-ДД");
            }
        }

        model.addAttribute("copy", copy);
        model.addAttribute("history", history);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "copyId";
    }

    @PostMapping("/copies/{copyId}/edit")
    public String updateCopy(
            @PathVariable Integer copyId,
            @RequestParam String availability,
            RedirectAttributes redirectAttributes) {

        Copy copy = copyDAO.getById(copyId);
        if (copy == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Экземпляр не найден");
            return "redirect:/books";
        }

        try {
            copy.setAvailability(availability);
            copyDAO.update(copy);
            redirectAttributes.addFlashAttribute("successMessage", "Статус экземпляра обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
        }

        return "redirect:/books/" + copy.getBook().getIsbn();
    }

    @PostMapping("/books/add")
    public String addBook(
            @Valid @ModelAttribute("newBook") Book newBook,
            BindingResult result,
            @RequestParam("authors") String authorsStr,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", result.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .collect(Collectors.joining("<br>")));
            return "redirect:/books/add";
        }

        try {
            newBook.setAuthors(Arrays.asList(authorsStr.split("\\s*,\\s*")));
            bookDAO.save(newBook);
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Книга успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Ошибка при добавлении: " + e.getMessage());
            return "redirect:/books/add";
        }

        return "redirect:/books";
    }

    @PostMapping("/books/{bookId}/addCopy")
    public String addCopy(
            @PathVariable("bookId") String bookId,
            RedirectAttributes redirectAttributes) {

        try {
            Book book = bookDAO.getById(bookId);
            if (book == null) {
                throw new RuntimeException("Книга не найдена");
            }

            Copy newCopy = new Copy();
            newCopy.setBook(book);
            newCopy.setAvailability("В наличии");

            copyDAO.save(newCopy);

            redirectAttributes.addFlashAttribute(
                    "successMessage", "Экземпляр успешно добавлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Ошибка: " + e.getMessage());
        }

        return "redirect:/books/{bookId}";
    }



    @PostMapping("/books/{bookId}/edit")
    public String updateBook(
            @PathVariable String bookId,
            @Valid @ModelAttribute("book") Book book,
            BindingResult result,
            @RequestParam("authors") String authorsStr,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", result.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .collect(Collectors.joining("<br>")));
            return "redirect:/books/" + bookId;
        }

        try {
            Book existingBook = bookDAO.getById(bookId);
            if (existingBook == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Книга не найдена");
                return "redirect:/books";
            }

            existingBook.setName(book.getName());
            existingBook.setAuthors(Arrays.asList(authorsStr.split("\\s*,\\s*")));
            existingBook.setPublisher(book.getPublisher());
            existingBook.setYear(book.getYear());

            bookDAO.update(existingBook);
            redirectAttributes.addFlashAttribute("successMessage", "Изменения сохранены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Ошибка при обновлении: " + e.getMessage());
        }

        return "redirect:/books";
    }

}