package com.bookstore.controller.api;

import com.bookstore.model.Warehouse;
import com.bookstore.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@Tag(name = "Warehouse", description = "API для управления складом")
public class WarehouseController {
    
    @Autowired
    private WarehouseService warehouseService;
    
    @GetMapping
    @Operation(summary = "Получить все записи склада", description = "Возвращает список всех записей склада")
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить запись склада по ID", description = "Возвращает запись склада по указанному ID")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }
    
    @GetMapping("/book/{bookId}")
    @Operation(summary = "Получить запись склада по книге", description = "Возвращает запись склада для указанной книги")
    public ResponseEntity<Warehouse> getWarehouseByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(warehouseService.getWarehouseByBookId(bookId));
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Получить товары с низким остатком", description = "Возвращает товары с количеством меньше или равным указанному")
    public ResponseEntity<List<Warehouse>> getLowStockItems(@RequestParam(defaultValue = "10") Integer threshold) {
        return ResponseEntity.ok(warehouseService.getLowStockItems(threshold));
    }
    
    @PostMapping
    @Operation(summary = "Создать запись склада", description = "Создает новую запись склада")
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        Warehouse created = warehouseService.createWarehouse(warehouse);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить запись склада", description = "Обновляет существующую запись склада")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, warehouse));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить запись склада", description = "Удаляет запись склада по ID")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}


