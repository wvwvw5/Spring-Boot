package com.bookstore.controller.api;

import com.bookstore.model.Genre;
import com.bookstore.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@Tag(name = "Genres", description = "API для управления жанрами")
public class GenreController {
    
    @Autowired
    private GenreService genreService;
    
    @GetMapping
    @Operation(summary = "Получить все жанры", description = "Возвращает список всех жанров")
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить жанр по ID", description = "Возвращает жанр по указанному ID")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.getGenreById(id));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск жанров", description = "Поиск жанров по названию")
    public ResponseEntity<List<Genre>> searchGenres(@RequestParam String name) {
        return ResponseEntity.ok(genreService.searchGenres(name));
    }
    
    @PostMapping
    @Operation(summary = "Создать жанр", description = "Создает новый жанр")
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        Genre created = genreService.createGenre(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить жанр", description = "Обновляет существующий жанр")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @Valid @RequestBody Genre genre) {
        return ResponseEntity.ok(genreService.updateGenre(id, genre));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить жанр", description = "Удаляет жанр по ID")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}


