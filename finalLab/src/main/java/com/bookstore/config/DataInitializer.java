package com.bookstore.config;

import com.bookstore.model.*;
import com.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Autowired
    private GenreRepository genreRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Создание ролей
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role managerRole = new Role("ROLE_MANAGER");
            Role userRole = new Role("ROLE_USER");
            
            roleRepository.save(adminRole);
            roleRepository.save(managerRole);
            roleRepository.save(userRole);
        }
        
        // Создание тестовых пользователей
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            Role managerRole = roleRepository.findByName("ROLE_MANAGER").orElseThrow();
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
            
            User admin = new User("admin", "admin@bookstore.com", 
                    passwordEncoder.encode("Admin123!"), "Администратор Системы");
            admin.setRoles(new HashSet<>(Set.of(adminRole)));
            userRepository.save(admin);
            
            User manager = new User("manager", "manager@bookstore.com", 
                    passwordEncoder.encode("Manager123!"), "Менеджер Магазина");
            manager.setRoles(new HashSet<>(Set.of(managerRole)));
            userRepository.save(manager);
            
            User user = new User("user", "user@bookstore.com", 
                    passwordEncoder.encode("User123!"), "Обычный Пользователь");
            user.setRoles(new HashSet<>(Set.of(userRole)));
            userRepository.save(user);
        }
        
        // Создание тестовых данных для книг
        if (bookRepository.count() == 0) {
            // Авторы
            Author author1 = new Author("Лев", "Толстой");
            author1.setBiography("Русский писатель, классик мировой литературы");
            authorRepository.save(author1);
            
            Author author2 = new Author("Фёдор", "Достоевский");
            author2.setBiography("Русский писатель, философ и публицист");
            authorRepository.save(author2);
            
            Author author3 = new Author("Александр", "Пушкин");
            author3.setBiography("Русский поэт, драматург и прозаик");
            authorRepository.save(author3);
            
            // Издательства
            Publisher publisher1 = new Publisher("Эксмо");
            publisher1.setAddress("Москва");
            publisher1.setEmail("info@eksmo.ru");
            publisherRepository.save(publisher1);
            
            Publisher publisher2 = new Publisher("АСТ");
            publisher2.setAddress("Москва");
            publisher2.setEmail("info@ast.ru");
            publisherRepository.save(publisher2);
            
            // Жанры
            Genre genre1 = new Genre("Роман");
            genre1.setDescription("Большое повествовательное произведение");
            genreRepository.save(genre1);
            
            Genre genre2 = new Genre("Поэзия");
            genre2.setDescription("Стихотворные произведения");
            genreRepository.save(genre2);
            
            Genre genre3 = new Genre("Классика");
            genre3.setDescription("Классическая литература");
            genreRepository.save(genre3);
            
            // Книги
            Book book1 = new Book("Война и мир", "978-5-17-123456-7", 
                    new BigDecimal("899.00"), 1274, 1869);
            book1.setDescription("Эпический роман Льва Толстого");
            book1.setPublisher(publisher1);
            book1.setAuthors(new HashSet<>(Set.of(author1)));
            book1.setGenres(new HashSet<>(Set.of(genre1, genre3)));
            bookRepository.save(book1);
            
            Book book2 = new Book("Преступление и наказание", "978-5-17-123457-4", 
                    new BigDecimal("599.00"), 671, 1866);
            book2.setDescription("Роман Фёдора Достоевского");
            book2.setPublisher(publisher2);
            book2.setAuthors(new HashSet<>(Set.of(author2)));
            book2.setGenres(new HashSet<>(Set.of(genre1, genre3)));
            bookRepository.save(book2);
            
            Book book3 = new Book("Евгений Онегин", "978-5-17-123458-1", 
                    new BigDecimal("399.00"), 224, 1833);
            book3.setDescription("Роман в стихах Александра Пушкина");
            book3.setPublisher(publisher1);
            book3.setAuthors(new HashSet<>(Set.of(author3)));
            book3.setGenres(new HashSet<>(Set.of(genre2, genre3)));
            bookRepository.save(book3);
            
            // Склад
            Warehouse warehouse1 = new Warehouse(book1, 50);
            warehouse1.setMinThreshold(10);
            warehouseRepository.save(warehouse1);
            
            Warehouse warehouse2 = new Warehouse(book2, 30);
            warehouse2.setMinThreshold(10);
            warehouseRepository.save(warehouse2);
            
            Warehouse warehouse3 = new Warehouse(book3, 40);
            warehouse3.setMinThreshold(10);
            warehouseRepository.save(warehouse3);
        }
    }
}


