package com.library.controller;

import com.library.model.Genre;
import com.library.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        List<Genre> genres;
        if (search != null && !search.isEmpty()) {
            genres = genreService.search(search);
        } else {
            genres = genreService.findAllSorted(sortDir);
        }
        model.addAttribute("genres", genres);
        model.addAttribute("search", search);
        model.addAttribute("sortDir", sortDir);
        return "genres/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("genre", new Genre());
        return "genres/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("genre") Genre genre,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "genres/form";
        }
        genreService.save(genre);
        redirectAttributes.addFlashAttribute("success", "Жанр успешно создан");
        return "redirect:/genres";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Genre genre = genreService.findById(id)
                .orElseThrow(() -> new RuntimeException("Жанр не найден"));
        model.addAttribute("genre", genre);
        model.addAttribute("booksCount", genreService.countBooks(id));
        return "genres/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Genre genre = genreService.findById(id)
                .orElseThrow(() -> new RuntimeException("Жанр не найден"));
        model.addAttribute("genre", genre);
        return "genres/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("genre") Genre genre,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "genres/form";
        }
        genreService.update(id, genre);
        redirectAttributes.addFlashAttribute("success", "Жанр успешно обновлён");
        return "redirect:/genres";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (genreService.hasBooks(id)) {
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить жанр: в нём есть книги (" + genreService.countBooks(id) + " шт.)");
            return "redirect:/genres";
        }
        genreService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Жанр успешно удалён");
        return "redirect:/genres";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam("ids") List<Long> ids,
                                 RedirectAttributes redirectAttributes) {
        StringBuilder errors = new StringBuilder();
        int deleted = 0;
        
        for (Long id : ids) {
            if (genreService.hasBooks(id)) {
                Genre genre = genreService.findById(id).orElse(null);
                if (genre != null) {
                    errors.append(genre.getName()).append(" (").append(genreService.countBooks(id)).append(" книг), ");
                }
            } else {
                genreService.delete(id);
                deleted++;
            }
        }
        
        if (errors.length() > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Не удалось удалить жанры с книгами: " + errors.toString());
        }
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("success", "Удалено жанров: " + deleted);
        }
        
        return "redirect:/genres";
    }
}

