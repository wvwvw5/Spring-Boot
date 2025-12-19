package com.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "warehouse")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Книга обязательна")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;
    
    @NotNull(message = "Количество на складе обязательно")
    @Min(value = 0, message = "Количество на складе не может быть отрицательным")
    @Column(nullable = false)
    private Integer quantity;
    
    @Min(value = 0, message = "Минимальный порог не может быть отрицательным")
    private Integer minThreshold;
    
    public Warehouse() {}
    
    public Warehouse(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getMinThreshold() {
        return minThreshold;
    }
    
    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }
    
    public Boolean isLowStock() {
        return minThreshold != null && quantity <= minThreshold;
    }
}


