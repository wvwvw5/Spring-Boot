package com.bookstore.controller.api;

import com.bookstore.model.Author;
import com.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "API для управления авторами")
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;
    
    @GetMapping
    @Operation(summary = "Получить всех авторов", description = "Возвращает список всех авторов")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить автора по ID", description = "Возвращает автора по указанному ID")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск авторов", description = "Поиск авторов по имени или фамилии")
    public ResponseEntity<List<Author>> searchAuthors(@RequestParam String name) {
        return ResponseEntity.ok(authorService.searchAuthors(name));
    }
    
    @PostMapping
    @Operation(summary = "Создать автора", description = "Создает нового автора")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        Author created = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить автора", description = "Обновляет существующего автора")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
        return ResponseEntity.ok(authorService.updateAuthor(id, author));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить автора", description = "Удаляет автора по ID")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}


