package com.library.repository;

import com.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Поиск по имени или фамилии
    @Query("SELECT a FROM Author a WHERE " +
           "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Author> searchByName(@Param("search") String search);

    // Поиск по стране
    List<Author> findByCountryIgnoreCase(String country);

    // Сортировка
    List<Author> findAllByOrderByLastNameAsc();
    List<Author> findAllByOrderByLastNameDesc();
    List<Author> findAllByOrderByFirstNameAsc();
    List<Author> findAllByOrderByBirthDateAsc();

    // Проверка наличия книг у автора
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.author.id = :authorId")
    boolean hasBooks(@Param("authorId") Long authorId);

    // Количество книг у автора
    @Query("SELECT COUNT(b) FROM Book b WHERE b.author.id = :authorId")
    long countBooks(@Param("authorId") Long authorId);
}

