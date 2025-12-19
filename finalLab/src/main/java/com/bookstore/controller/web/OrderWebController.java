package com.bookstore.controller.web;

import com.bookstore.model.*;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/web/orders")
public class OrderWebController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String listOrders(Model model, Authentication authentication) {
        List<Order> orders;
        boolean isOnlyUser = authentication != null && 
                            authentication.getAuthorities().stream()
                                .allMatch(a -> a.getAuthority().equals("ROLE_USER"));
        
        if (isOnlyUser) {
            // Для обычных пользователей показываем только их заказы
            User currentUser = userService.getUserByUsername(authentication.getName());
            orders = orderService.getOrdersByUser(currentUser.getId());
        } else {
            // Для админов и менеджеров показываем все заказы
            orders = orderService.getAllOrders();
        }
        model.addAttribute("orders", orders);
        setRoleAttributes(model, authentication);
        return "orders/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model, Authentication authentication) {
        Order order = new Order();
        order.setOrderItems(new HashSet<>());
        model.addAttribute("order", order);
        
        // Для обычных пользователей показываем только их аккаунт
        boolean isOnlyUser = authentication != null && 
                            authentication.getAuthorities().stream()
                                .allMatch(a -> a.getAuthority().equals("ROLE_USER"));
        
        if (isOnlyUser) {
            User currentUser = userService.getUserByUsername(authentication.getName());
            model.addAttribute("users", List.of(currentUser));
            model.addAttribute("isUserOrder", true);
        } else {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("isUserOrder", false);
        }
        
        model.addAttribute("books", bookService.getAllBooks());
        setRoleAttributes(model, authentication);
        return "orders/form";
    }
    
    @PostMapping
    public String createOrder(@ModelAttribute Order order,
                             @RequestParam Long userId,
                             @RequestParam(required = false) List<Long> bookIds,
                             @RequestParam(required = false) List<Integer> quantities,
                             @RequestParam(required = false) String shippingAddress,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            
            // Проверка: обычные пользователи могут создавать заказы только для себя
            boolean isOnlyUser = authentication != null && 
                                authentication.getAuthorities().stream()
                                    .allMatch(a -> a.getAuthority().equals("ROLE_USER"));
            
            if (isOnlyUser) {
                User currentUser = userService.getUserByUsername(authentication.getName());
                if (!user.getId().equals(currentUser.getId())) {
                    redirectAttributes.addFlashAttribute("error", "Вы можете создавать заказы только для себя");
                    return "redirect:/web/orders/new";
                }
            }
            
            order.setUser(user);
            order.setShippingAddress(shippingAddress);
            
            Set<OrderItem> orderItems = new HashSet<>();
            if (bookIds != null && quantities != null && bookIds.size() == quantities.size()) {
                for (int i = 0; i < bookIds.size(); i++) {
                    Long bookId = bookIds.get(i);
                    Integer quantity = quantities.get(i);
                    if (bookId != null && quantity != null && quantity > 0) {
                        Book book = bookService.getBookById(bookId);
                        OrderItem item = new OrderItem();
                        item.setBook(book);
                        item.setQuantity(quantity);
                        item.setPrice(book.getPrice());
                        item.setOrder(order);
                        orderItems.add(item);
                    }
                }
            }
            order.setOrderItems(orderItems);
            
            orderService.createOrder(order);
            redirectAttributes.addFlashAttribute("success", "Заказ успешно создан!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/orders/new";
        }
        return "redirect:/web/orders";
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, Authentication authentication) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        setRoleAttributes(model, authentication);
        return "orders/view";
    }
    
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, 
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            orderService.updateOrderStatus(id, orderStatus);
            redirectAttributes.addFlashAttribute("success", "Статус заказа обновлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/orders/" + id;
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

