package com.library.repository;

import com.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Поиск по названию
    List<Genre> findByNameContainingIgnoreCase(String name);

    // Сортировка
    List<Genre> findAllByOrderByNameAsc();
    List<Genre> findAllByOrderByNameDesc();

    // Проверка наличия книг в жанре
    @Query("SELECT COUNT(b) > 0 FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    boolean hasBooks(@Param("genreId") Long genreId);

    // Количество книг в жанре
    @Query("SELECT COUNT(b) FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    long countBooks(@Param("genreId") Long genreId);
}

