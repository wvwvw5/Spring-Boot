package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Издательство"
 * Связь: OneToMany с Book
 */
@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название издательства обязательно")
    @Size(min = 2, max = 200, message = "Название должно быть от 2 до 200 символов")
    @Column(nullable = false)
    private String name;

    @Size(max = 200, message = "Адрес не должен превышать 200 символов")
    private String address;

    @Size(max = 100, message = "Город не должен превышать 100 символов")
    private String city;

    @Size(max = 100, message = "Страна не должна превышать 100 символов")
    private String country;

    @Email(message = "Некорректный email")
    @Size(max = 100)
    private String email;

    @Pattern(regexp = "^[+]?[0-9\\-\\s()]*$", message = "Некорректный номер телефона")
    @Size(max = 20)
    private String phone;

    // Связь один-ко-многим с Book
    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY)
    private List<Book> books = new ArrayList<>();

    // Конструкторы
    public Publisher() {}

    public Publisher(String name, String city) {
        this.name = name;
        this.city = city;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}

