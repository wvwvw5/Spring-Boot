package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Warehouse;
import com.bookstore.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WarehouseService {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private BookService bookService;
    
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
    
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись склада с ID " + id + " не найдена"));
    }
    
    public Warehouse getWarehouseByBookId(Long bookId) {
        Warehouse warehouse = warehouseRepository.findByBookId(bookId);
        if (warehouse == null) {
            throw new ResourceNotFoundException("Книга с ID " + bookId + " не найдена на складе");
        }
        return warehouse;
    }
    
    public List<Warehouse> getLowStockItems(Integer threshold) {
        return warehouseRepository.findByQuantityLessThanEqual(threshold);
    }
    
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании записи склада");
        }
        if (warehouse.getBook() == null || warehouse.getBook().getId() == null) {
            throw new ValidationException("Книга обязательна");
        }
        warehouse.setBook(bookService.getBookById(warehouse.getBook().getId()));
        return warehouseRepository.save(warehouse);
    }
    
    public Warehouse updateWarehouse(Long id, Warehouse warehouseDetails) {
        Warehouse warehouse = getWarehouseById(id);
        warehouse.setQuantity(warehouseDetails.getQuantity());
        warehouse.setMinThreshold(warehouseDetails.getMinThreshold());
        return warehouseRepository.save(warehouse);
    }
    
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = getWarehouseById(id);
        warehouseRepository.delete(warehouse);
    }
}


