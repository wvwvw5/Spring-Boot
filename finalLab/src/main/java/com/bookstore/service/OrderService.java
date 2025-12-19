package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.*;
import com.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private WarehouseService warehouseService;
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заказ с ID " + id + " не найден"));
    }
    
    public List<Order> getOrdersByUser(Long userId) {
        userService.getUserById(userId);
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public Order createOrder(Order order) {
        if (order.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании заказа");
        }
        
        // Проверяем наличие книг на складе и уменьшаем количество
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            Book book = bookService.getBookById(item.getBook().getId());
            Warehouse warehouse = warehouseService.getWarehouseByBookId(book.getId());
            
            if (warehouse.getQuantity() < item.getQuantity()) {
                throw new ValidationException("Недостаточно книг на складе: " + book.getTitle());
            }
            
            warehouse.setQuantity(warehouse.getQuantity() - item.getQuantity());
            warehouseService.updateWarehouse(warehouse.getId(), warehouse);
            
            item.setPrice(book.getPrice());
            item.setBook(book);
            totalAmount = totalAmount.add(item.getSubtotal());
        }
        
        order.setTotalAmount(totalAmount);
        order.setUser(userService.getUserById(order.getUser().getId()));
        
        return orderRepository.save(order);
    }
    
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new ValidationException("Невозможно удалить доставленный заказ");
        }
        orderRepository.delete(order);
    }
}


