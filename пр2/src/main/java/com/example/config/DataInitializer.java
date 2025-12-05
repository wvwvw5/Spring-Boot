package com.example.config;

import com.example.model.Category;
import com.example.model.Product;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Инициализация тестовых данных при запуске приложения
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Создание категорий
        Category electronics = new Category("Электроника", "Смартфоны, ноутбуки, планшеты");
        Category clothing = new Category("Одежда", "Мужская и женская одежда");
        Category books = new Category("Книги", "Художественная и учебная литература");
        
        categoryRepository.save(electronics);
        categoryRepository.save(clothing);
        categoryRepository.save(books);

        // Создание товаров
        Product phone = new Product("iPhone 15", "Смартфон Apple iPhone 15 128GB", 
                new BigDecimal("89990.00"), 50);
        phone.setCategory(electronics);
        
        Product laptop = new Product("MacBook Pro", "Ноутбук Apple MacBook Pro 14\"", 
                new BigDecimal("199990.00"), 20);
        laptop.setCategory(electronics);
        
        Product tshirt = new Product("Футболка Nike", "Спортивная футболка Nike Dri-FIT", 
                new BigDecimal("2990.00"), 100);
        tshirt.setCategory(clothing);
        
        Product jeans = new Product("Джинсы Levi's", "Классические джинсы Levi's 501", 
                new BigDecimal("7990.00"), 75);
        jeans.setCategory(clothing);
        
        Product book1 = new Product("Война и мир", "Роман Л.Н. Толстого", 
                new BigDecimal("890.00"), 200);
        book1.setCategory(books);
        
        Product book2 = new Product("Java. Полное руководство", "Учебник по программированию", 
                new BigDecimal("2490.00"), 30);
        book2.setCategory(books);

        productRepository.save(phone);
        productRepository.save(laptop);
        productRepository.save(tshirt);
        productRepository.save(jeans);
        productRepository.save(book1);
        productRepository.save(book2);
    }
}


