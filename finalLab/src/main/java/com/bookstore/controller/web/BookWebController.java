package com.bookstore.controller.web;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.model.Genre;
import com.bookstore.model.Publisher;
import com.bookstore.service.AuthorService;
import com.bookstore.service.BookService;
import com.bookstore.service.GenreService;
import com.bookstore.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/web/books")
public class BookWebController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private GenreService genreService;
    
    @GetMapping
    public String listBooks(Model model, Authentication authentication) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        setRoleAttributes(model, authentication);
        return "books/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        Book book = new Book();
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("genres", genreService.getAllGenres());
        setRoleAttributes(model, authentication);
        return "books/form";
    }
    
    @PostMapping
    public String createBook(@ModelAttribute Book book, 
                            @RequestParam(required = false) Long publisherId,
                            @RequestParam(required = false) List<Long> authorIds,
                            @RequestParam(required = false) List<Long> genreIds,
                            RedirectAttributes redirectAttributes) {
        try {
            if (publisherId != null) {
                book.setPublisher(publisherService.getPublisherById(publisherId));
            }
            if (authorIds != null && !authorIds.isEmpty()) {
                Set<Author> authors = new HashSet<>();
                for (Long id : authorIds) {
                    authors.add(authorService.getAuthorById(id));
                }
                book.setAuthors(authors);
            }
            if (genreIds != null && !genreIds.isEmpty()) {
                Set<Genre> genres = new HashSet<>();
                for (Long id : genreIds) {
                    genres.add(genreService.getGenreById(id));
                }
                book.setGenres(genres);
            }
            bookService.createBook(book);
            redirectAttributes.addFlashAttribute("success", "Книга успешно создана!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/books";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("publishers", publisherService.getAllPublishers());
        model.addAttribute("genres", genreService.getAllGenres());
        setRoleAttributes(model, authentication);
        return "books/form";
    }
    
    @PostMapping("/{id}")
    public String updateBook(@PathVariable Long id, 
                            @ModelAttribute Book book,
                            @RequestParam(required = false) Long publisherId,
                            @RequestParam(required = false) List<Long> authorIds,
                            @RequestParam(required = false) List<Long> genreIds,
                            RedirectAttributes redirectAttributes) {
        try {
            if (publisherId != null) {
                book.setPublisher(publisherService.getPublisherById(publisherId));
            }
            if (authorIds != null && !authorIds.isEmpty()) {
                Set<Author> authors = new HashSet<>();
                for (Long authorId : authorIds) {
                    authors.add(authorService.getAuthorById(authorId));
                }
                book.setAuthors(authors);
            }
            if (genreIds != null && !genreIds.isEmpty()) {
                Set<Genre> genres = new HashSet<>();
                for (Long genreId : genreIds) {
                    genres.add(genreService.getGenreById(genreId));
                }
                book.setGenres(genres);
            }
            bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("success", "Книга успешно обновлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/books";
    }
    
    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model, Authentication authentication) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        setRoleAttributes(model, authentication);
        return "books/view";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("success", "Книга успешно удалена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/books";
    }
    
    private void setRoleAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends org.springframework.security.core.GrantedAuthority> authorities = authentication.getAuthorities();
            boolean isAdmin = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isManager = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
            boolean isUser = authorities.stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
            
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("isManager", isManager);
            model.addAttribute("isUser", isUser);
            model.addAttribute("username", authentication.getName());
        } else {
            model.addAttribute("isAdmin", false);
            model.addAttribute("isManager", false);
            model.addAttribute("isUser", false);
        }
    }
}

