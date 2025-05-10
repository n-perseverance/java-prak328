package msu.cmc.webprak.Controllers;

import msu.cmc.webprak.DAO.CopyDAO;
import msu.cmc.webprak.java_entities.Book;
import msu.cmc.webprak.java_entities.Copy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CopyController {

    @Autowired
    private CopyDAO copyDAO;

    @GetMapping("/copies")
    public String showCopyForm(@PathVariable Integer copyId, Model model) {
        Copy copy = copyDAO.getById(copyId);
        if (copy == null) {
            return "error";
        }
        model.addAttribute("copy", copy);
        return "copyEdit";
    }

    @PostMapping("/{copyId}/update")
    public String updateCopy(
            @PathVariable Integer copyId,
            @RequestParam String availability,
            RedirectAttributes redirectAttributes) {

        Copy copy = copyDAO.getById(copyId);
        if (copy == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Экземпляр не найден");
            return "redirect:/books";
        }

        copy.setAvailability(availability);
        copyDAO.update(copy);

        redirectAttributes.addFlashAttribute("successMessage", "Статус обновлен");
        return "redirect:/books/" + copy.getBook().getIsbn();
    }
}