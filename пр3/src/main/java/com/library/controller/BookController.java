package com.library.controller;

import com.library.model.Book;
import com.library.model.Genre;
import com.library.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenreService genreService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService,
                          PublisherService publisherService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genreService = genreService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Long authorId,
                       @RequestParam(required = false) Long publisherId,
                       @RequestParam(required = false) Long genreId,
                       @RequestParam(required = false) Integer yearFrom,
                       @RequestParam(required = false) Integer yearTo,
                       @RequestParam(required = false, defaultValue = "title") String sortBy,
                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                       Model model) {
        
        List<Book> books;
        if ((search != null && !search.isEmpty()) || authorId != null || 
            publisherId != null || genreId != null || yearFrom != null || yearTo != null) {
            books = bookService.findByFilters(search, authorId, publisherId, genreId, yearFrom, yearTo);
        } else {
            books = bookService.findAllSorted(sortBy, sortDir);
        }
        
        model.addAttribute("books", books);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("search", search);
        model.addAttribute("authorId", authorId);
        model.addAttribute("publisherId", publisherId);
        model.addAttribute("genreId", genreId);
        model.addAttribute("yearFrom", yearFrom);
        model.addAttribute("yearTo", yearTo);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "books/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         @RequestParam(required = false) Long authorId,
                         @RequestParam(required = false) Long publisherId,
                         @RequestParam(required = false) List<Long> genreIds,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/form";
        }
        
        setBookRelations(book, authorId, publisherId, genreIds);
        bookService.save(book);
        redirectAttributes.addFlashAttribute("success", "Книга успешно создана");
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        model.addAttribute("book", book);
        model.addAttribute("activeLoans", bookService.countActiveLoans(id));
        return "books/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("publishers", publisherService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("book") Book book,
                         BindingResult result,
                         @RequestParam(required = false) Long authorId,
                         @RequestParam(required = false) Long publisherId,
                         @RequestParam(required = false) List<Long> genreIds,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("publishers", publisherService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/form";
        }
        
        setBookRelations(book, authorId, publisherId, genreIds);
        bookService.update(id, book);
        redirectAttributes.addFlashAttribute("success", "Книга успешно обновлена");
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Проверка активных выдач перед удалением
        if (bookService.hasActiveLoans(id)) {
            redirectAttributes.addFlashAttribute("error", 
                "Невозможно удалить книгу: есть активные выдачи (" + bookService.countActiveLoans(id) + " шт.)");
            return "redirect:/books";
        }
        bookService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Книга успешно удалена");
        return "redirect:/books";
    }

    @PostMapping("/delete-multiple")
    public String deleteMultiple(@RequestParam("ids") List<Long> ids,
                                 RedirectAttributes redirectAttributes) {
        StringBuilder errors = new StringBuilder();
        int deleted = 0;
        
        for (Long id : ids) {
            if (bookService.hasActiveLoans(id)) {
                Book book = bookService.findById(id).orElse(null);
                if (book != null) {
                    errors.append(book.getTitle()).append(" (").append(bookService.countActiveLoans(id)).append(" выдач), ");
                }
            } else {
                bookService.delete(id);
                deleted++;
            }
        }
        
        if (errors.length() > 0) {
            redirectAttributes.addFlashAttribute("error", 
                "Не удалось удалить книги с активными выдачами: " + errors.toString());
        }
        if (deleted > 0) {
            redirectAttributes.addFlashAttribute("success", "Удалено книг: " + deleted);
        }
        
        return "redirect:/books";
    }

    private void setBookRelations(Book book, Long authorId, Long publisherId, List<Long> genreIds) {
        if (authorId != null) {
            authorService.findById(authorId).ifPresent(book::setAuthor);
        } else {
            book.setAuthor(null);
        }
        
        if (publisherId != null) {
            publisherService.findById(publisherId).ifPresent(book::setPublisher);
        } else {
            book.setPublisher(null);
        }
        
        if (genreIds != null && !genreIds.isEmpty()) {
            List<Genre> genres = new ArrayList<>();
            for (Long genreId : genreIds) {
                genreService.findById(genreId).ifPresent(genres::add);
            }
            book.setGenres(genres);
        } else {
            book.setGenres(new ArrayList<>());
        }
    }
}


