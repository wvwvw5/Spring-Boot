package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Автор"
 * Связь: OneToMany с Book
 */
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя автора обязательно")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия автора обязательна")
    @Size(min = 2, max = 100, message = "Фамилия должна быть от 2 до 100 символов")
    @Column(nullable = false)
    private String lastName;

    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthDate;

    @Size(max = 100, message = "Страна не должна превышать 100 символов")
    private String country;

    @Size(max = 1000, message = "Биография не должна превышать 1000 символов")
    @Column(length = 1000)
    private String biography;

    // Связь один-ко-многим с Book
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    // Конструкторы
    public Author() {}

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Вспомогательный метод
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}


