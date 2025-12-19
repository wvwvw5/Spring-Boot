package com.bookstore.controller.api;

import com.bookstore.model.Publisher;
import com.bookstore.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@Tag(name = "Publishers", description = "API для управления издательствами")
public class PublisherController {
    
    @Autowired
    private PublisherService publisherService;
    
    @GetMapping
    @Operation(summary = "Получить все издательства", description = "Возвращает список всех издательств")
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить издательство по ID", description = "Возвращает издательство по указанному ID")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск издательств", description = "Поиск издательств по названию")
    public ResponseEntity<List<Publisher>> searchPublishers(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.searchPublishers(name));
    }
    
    @PostMapping
    @Operation(summary = "Создать издательство", description = "Создает новое издательство")
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) {
        Publisher created = publisherService.createPublisher(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить издательство", description = "Обновляет существующее издательство")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @Valid @RequestBody Publisher publisher) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisher));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить издательство", description = "Удаляет издательство по ID")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}


