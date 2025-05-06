package msu.cmc.webprak.Controllers;

import jakarta.validation.Valid;
import msu.cmc.webprak.DAO.ReaderDAO;
import msu.cmc.webprak.DAO.HistoryDAO;
import msu.cmc.webprak.java_entities.Reader;
import msu.cmc.webprak.java_entities.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ReaderController {
    @Autowired
    ReaderDAO readerDAO;

    @GetMapping("/readers")
    public String listReaders(Model model,
                                @RequestParam(value = "name", required = false) String searchName,
                                @RequestParam(value = "phone", required = false) String searchPhone) {

        List<Reader> readers;
        if (searchName != null && !searchName.isEmpty() || searchPhone != null && !searchPhone.isEmpty()) {
            readers = readerDAO.searchReaders(searchName, searchPhone);
        } else {
            readers = readerDAO.getAll();
        }

        model.addAttribute("readers", readers);
        model.addAttribute("searchName",searchName);
        model.addAttribute("searchPhone",searchPhone);
        return "readers";
    }


    @GetMapping("/readers/{readerId}")
    public String showReader(@PathVariable Integer readerId, Model model) {
        Reader reader = readerDAO.getById(readerId);
        if (reader == null) {
            return "error";
        }
        model.addAttribute("reader", reader);
        model.addAttribute("history",readerDAO.historyById(readerId));
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

        return "redirect:/readers/{readerId}";
    }


}