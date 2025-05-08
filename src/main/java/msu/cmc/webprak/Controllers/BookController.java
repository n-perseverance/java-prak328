package msu.cmc.webprak.Controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
    @GetMapping("/books/{bookId}")
    public String showEditForm(@PathVariable String bookId, Model model) {
        Book book = bookDAO.getById(bookId);
        model.addAttribute("book", book);
        return "bookId";
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