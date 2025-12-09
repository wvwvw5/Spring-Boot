package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Жанр"
 * Связь: ManyToMany с Book
 */
@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название жанра обязательно")
    @Size(min = 2, max = 100, message = "Название должно быть от 2 до 100 символов")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    @Column(length = 500)
    private String description;

    // Связь многие-ко-многим с Book
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    // Конструкторы
    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}


