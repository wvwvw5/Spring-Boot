package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Книга"
 * Связи:
 * - ManyToOne с Author
 * - ManyToOne с Publisher
 * - ManyToMany с Genre
 * - OneToMany с BookLoan
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название книги обязательно")
    @Size(min = 1, max = 300, message = "Название должно быть от 1 до 300 символов")
    @Column(nullable = false)
    private String title;

    @Size(max = 20, message = "ISBN не должен превышать 20 символов")
    @Column(unique = true)
    private String isbn;

    @Min(value = 1000, message = "Год издания должен быть не ранее 1000")
    @Max(value = 2100, message = "Год издания должен быть не позднее 2100")
    private Integer publicationYear;

    @Min(value = 1, message = "Количество страниц должно быть больше 0")
    private Integer pages;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    @Column(length = 2000)
    private String description;

    @DecimalMin(value = "0.00", message = "Цена не может быть отрицательной")
    @Digits(integer = 8, fraction = 2)
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Min(value = 0, message = "Количество копий не может быть отрицательным")
    @Column(nullable = false)
    private Integer totalCopies = 1;

    @Min(value = 0, message = "Количество доступных копий не может быть отрицательным")
    @Column(nullable = false)
    private Integer availableCopies = 1;

    // Связь многие-к-одному с Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    // Связь многие-к-одному с Publisher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    // Связь многие-ко-многим с Genre
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_genres",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    // Связь один-ко-многим с BookLoan
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookLoan> loans = new ArrayList<>();

    // Конструкторы
    public Book() {}

    public Book(String title, String isbn) {
        this.title = title;
        this.isbn = isbn;
    }

    // Вспомогательные методы
    public boolean isAvailable() {
        return availableCopies > 0;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }

    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public List<BookLoan> getLoans() { return loans; }
    public void setLoans(List<BookLoan> loans) { this.loans = loans; }
}


