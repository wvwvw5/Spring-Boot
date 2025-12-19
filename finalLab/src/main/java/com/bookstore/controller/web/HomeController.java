package com.bookstore.controller.web;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/")
    public String index(Authentication authentication) {
        // Если пользователь авторизован, перенаправляем на /home
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        // Иначе показываем лендинг
        return "landing";
    }
    
    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
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
        
        // Загружаем популярные книги (первые 6 книг)
        // Обрабатываем ошибки, чтобы страница загружалась даже если БД пустая или есть проблемы
        try {
            List<Book> allBooks = bookService.getAllBooks();
            if (allBooks != null && !allBooks.isEmpty()) {
                List<Book> popularBooks = allBooks.stream()
                        .limit(6)
                        .collect(Collectors.toList());
                model.addAttribute("popularBooks", popularBooks);
            } else {
                model.addAttribute("popularBooks", List.of());
            }
        } catch (Exception e) {
            // Если произошла ошибка при загрузке книг, просто показываем пустой список
            logger.error("Ошибка при загрузке книг для главной страницы", e);
            model.addAttribute("popularBooks", List.of());
        }
        
        return "home";
    }
}

