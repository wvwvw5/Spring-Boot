package com.bookstore.controller.web;

import com.bookstore.model.Book;
import com.bookstore.model.Warehouse;
import com.bookstore.service.BookService;
import com.bookstore.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/web/warehouse")
public class WarehouseWebController {
    
    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String listWarehouse(Model model, Authentication authentication) {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        model.addAttribute("warehouses", warehouses);
        setRoleAttributes(model, authentication);
        return "warehouse/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        model.addAttribute("warehouse", new Warehouse());
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        setRoleAttributes(model, authentication);
        return "warehouse/form";
    }
    
    @PostMapping
    public String createWarehouse(@ModelAttribute Warehouse warehouse,
                                 RedirectAttributes redirectAttributes) {
        try {
            warehouseService.createWarehouse(warehouse);
            redirectAttributes.addFlashAttribute("success", "Запись склада успешно создана!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/warehouse";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        model.addAttribute("warehouse", warehouse);
        setRoleAttributes(model, authentication);
        return "warehouse/form";
    }
    
    @PostMapping("/{id}")
    public String updateWarehouse(@PathVariable Long id,
                                 @ModelAttribute Warehouse warehouse,
                                 RedirectAttributes redirectAttributes) {
        try {
            warehouseService.updateWarehouse(id, warehouse);
            redirectAttributes.addFlashAttribute("success", "Запись склада успешно обновлена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/warehouse";
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


