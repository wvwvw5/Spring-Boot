package com.library.controller;

import com.library.model.Author;
import com.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false, defaultValue = "lastName") String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        List<Author> authors;
        if (search != null && !search.isEmpty()) {
            authors = authorService.search(search);
        } else {
            authors = authorService.findAllSorted(sortBy, sortDir);
        }
        model.addAttribute("authors", authors);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "authors/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("author", new Author());
        return "authors/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("author") Author author,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "authors/form";
        }
        authorService.save(author);
        redirectAttributes.addFlashAttribute("success", "Автор успешно создан");
        return "redirect:/authors";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Author author = authorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        model.addAttribute("author", author);
        model.addAttribute("booksCount", authorService.countBooks(id));
        return "authors/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Author author = authorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        model.addAttribute("author", author);
        return "authors/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("author") Author author,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "authors/form";
        }
        authorService.update(id, author);
        redirectAttributes.addFlashAttribute("success", "Автор успешно обновлён");
        return "redirect:/authors";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Проверка связей перед удалением
        if (authorService.hasBooks(id)) {
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить автора: у него есть книги (" + authorService.countBooks(id) + " шт.)");
            return "redirect:/authors";
        }
        authorService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Автор успешно удалён");
        return "redirect:/authors";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam("ids") List<Long> ids,
                                 RedirectAttributes redirectAttributes) {
        // Проверка связей для каждого автора
        StringBuilder errors = new StringBuilder();
        int deleted = 0;
        
        for (Long id : ids) {
            if (authorService.hasBooks(id)) {
                Author author = authorService.findById(id).orElse(null);
                if (author != null) {
                    errors.append(author.getFullName()).append(" (").append(authorService.countBooks(id)).append(" книг), ");
                }
            } else {
                authorService.delete(id);
                deleted++;
            }
        }
        
        if (errors.length() > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Не удалось удалить авторов с книгами: " + errors.toString());
        }
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("success", "Удалено авторов: " + deleted);
        }
        
        return "redirect:/authors";
    }
}


