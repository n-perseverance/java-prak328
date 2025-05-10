package msu.cmc.webprak.Controllers;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import msu.cmc.webprak.DAO.ReaderDAO;
import msu.cmc.webprak.DAO.HistoryDAO;
import msu.cmc.webprak.DAO.BookDAO;
import msu.cmc.webprak.DAO.CopyDAO;
import msu.cmc.webprak.java_entities.Reader;
import msu.cmc.webprak.java_entities.History;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.Copy;
import msu.cmc.webprak.java_entities.HistoryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReaderController {
    @Autowired
    ReaderDAO readerDAO;

    @Autowired
    BookDAO bookDAO;

    @Autowired
    HistoryDAO historyDAO;

    @Autowired
    CopyDAO copyDAO;

    @GetMapping("/readers")
    public String listReaders(Model model,
                                @RequestParam(value = "name", required = false) String searchName,
                                @RequestParam(value = "id", required = false) String searchId,
                                @RequestParam(value = "phone", required = false) String searchPhone) {

        List<Reader> readers;
        if (searchName != null && !searchName.isEmpty() || searchPhone != null && !searchPhone.isEmpty() || searchId != null && !searchId.isEmpty()) {
            readers = readerDAO.searchReaders(searchName, searchPhone, searchId);
        } else {
            readers = readerDAO.getAll();
        }

        model.addAttribute("readers", readers);
        model.addAttribute("searchName",searchName);
        model.addAttribute("searchPhone",searchPhone);
        model.addAttribute("searchId",searchId);
        return "readers";
    }


    @GetMapping("/readers/{readerId}")
    public String showReader(@PathVariable Integer readerId, Model model) {
        Reader reader = readerDAO.getById(readerId);
        if (reader == null) {
            return "error";
        }
        model.addAttribute("reader", reader);
        model.addAttribute("history", readerDAO.historyById(readerId));
        return "readerId";
    }

    @GetMapping("/readers/add")
    public String addReaderForm(Model model) {
        model.addAttribute("newReader", new Reader());
        return "addReader";
    }

    @GetMapping("readers/{readerId}/delete")
    public String deleteReader(Model model, @PathVariable Integer readerId) {
        readerDAO.deleteById(readerId);
        return "redirect:/readers";
    }

    @GetMapping("/readers/{readerId}/addHistory")
    public String showAddHistoryForm(@PathVariable Integer readerId, Model model) {
        try {
            Reader reader = readerDAO.getById(readerId);
            if (reader == null) {
                throw new RuntimeException("Читатель не найден");
            }

            History history = new History();
            history.setReader(reader);
            history.setIssueDate(new Date());

            List<Book> allBooks = bookDAO.getAll();

            model.addAttribute("reader", reader);
            model.addAttribute("history", history);
            model.addAttribute("allBooks", allBooks);

            return "addHistory";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке формы: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/readers/{readerId}/history/{isbn}/{copyId}/delete")
    public String deleteHistoryRecord(
            @PathVariable Integer readerId,
            @PathVariable String isbn,
            @PathVariable Integer copyId,
            RedirectAttributes redirectAttributes) {

        try {
            HistoryId historyId = new HistoryId(isbn, copyId, readerId);
            History history = historyDAO.getById(historyId);

            if (history == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Запись истории не найдена");
                return "redirect:/readers/" + readerId;
            }

            historyDAO.delete(history);
            redirectAttributes.addFlashAttribute("successMessage", "Запись истории успешно удалена");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении: " + e.getMessage());
        }

        return "redirect:/readers/" + readerId;
    }

    @PostMapping("/readers/{readerId}/addHistory")
    @Transactional
    public String addHistory(
            @PathVariable Integer readerId,
            @RequestParam("isbn") String isbn,
            @RequestParam("copyId") Integer copyId,
            @RequestParam("issueDate") String issueDateStr,
            @RequestParam("returnDate") String returnDateStr,
            RedirectAttributes redirectAttributes) {

        try {
            Reader reader = readerDAO.getById(readerId);
            Book book = bookDAO.getById(isbn);
            Copy copy = copyDAO.getById(copyId);

            if (reader == null || book == null || copy == null) {
                throw new RuntimeException("Один из объектов не найден");
            }

            if (!copy.getBook().getIsbn().equals(isbn)) {
                throw new RuntimeException("Экземпляр не принадлежит указанной книге");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = dateFormat.parse(issueDateStr);
            Date returnDate = dateFormat.parse(returnDateStr);

            if (returnDate.before(issueDate)) {
                throw new RuntimeException("Дата возврата не может быть раньше даты выдачи");
            }

            History history = new History();
            history.setId(new HistoryId(isbn, copyId, readerId));
            history.setBook(book);
            history.setCopy(copy);
            history.setReader(reader);
            history.setIssueDate(issueDate);
            history.setReturnDate(returnDate);

            historyDAO.save(history);

            redirectAttributes.addFlashAttribute("successMessage", "Запись истории успешно добавлена");

        } catch (ParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Неверный формат даты");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
        }

        return "redirect:/readers/" + readerId;
    }


    @PostMapping("/readers/add")
    public String addReader(
            @Valid @ModelAttribute("newReaders") Reader newReader,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("<br>"));
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/readers/add";
        }

        try {
            readerDAO.save(newReader);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении: " + e.getMessage());
            return "redirect:/readers/add";
        }

        return "redirect:/readers";
    }

    @PostMapping("/readers/{readerId}/edit")
    public String updateReader(@PathVariable Integer readerId, @Valid @ModelAttribute("reader") Reader reader,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        Reader existingReader = readerDAO.getById(readerId);
        if (existingReader == null) {
            return "error";
        }

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("<br>"));
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/readers/{readerId}";
        }

        existingReader.setName(reader.getName());
        existingReader.setPhoneNumber(reader.getPhoneNumber());
        existingReader.setAddress(reader.getAddress());


        try {
            readerDAO.update(existingReader);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
        }

        return "redirect:/readers";
    }


}