package com.library.controller;

import com.library.model.Publisher;
import com.library.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false, defaultValue = "name") String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        List<Publisher> publishers;
        if (search != null && !search.isEmpty()) {
            publishers = publisherService.search(search);
        } else {
            publishers = publisherService.findAllSorted(sortBy, sortDir);
        }
        model.addAttribute("publishers", publishers);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "publishers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("publisher", new Publisher());
        return "publishers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("publisher") Publisher publisher,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "publishers/form";
        }
        publisherService.save(publisher);
        redirectAttributes.addFlashAttribute("success", "Издательство успешно создано");
        return "redirect:/publishers";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Publisher publisher = publisherService.findById(id)
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
        model.addAttribute("publisher", publisher);
        model.addAttribute("booksCount", publisherService.countBooks(id));
        return "publishers/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Publisher publisher = publisherService.findById(id)
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
        model.addAttribute("publisher", publisher);
        return "publishers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("publisher") Publisher publisher,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "publishers/form";
        }
        publisherService.update(id, publisher);
        redirectAttributes.addFlashAttribute("success", "Издательство успешно обновлено");
        return "redirect:/publishers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (publisherService.hasBooks(id)) {
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить издательство: у него есть книги (" + publisherService.countBooks(id) + " шт.)");
            return "redirect:/publishers";
        }
        publisherService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Издательство успешно удалено");
        return "redirect:/publishers";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam("ids") List<Long> ids,
                                 RedirectAttributes redirectAttributes) {
        StringBuilder errors = new StringBuilder();
        int deleted = 0;
        
        for (Long id : ids) {
            if (publisherService.hasBooks(id)) {
                Publisher publisher = publisherService.findById(id).orElse(null);
                if (publisher != null) {
                    errors.append(publisher.getName()).append(" (").append(publisherService.countBooks(id)).append(" книг), ");
                }
            } else {
                publisherService.delete(id);
                deleted++;
            }
        }
        
        if (errors.length() > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Не удалось удалить издательства с книгами: " + errors.toString());
        }
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("success", "Удалено издательств: " + deleted);
        }
        
        return "redirect:/publishers";
    }
}


