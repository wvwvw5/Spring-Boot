package com.library.repository;

import com.library.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    // Поиск по названию
    List<Publisher> findByNameContainingIgnoreCase(String name);

    // Поиск по городу
    List<Publisher> findByCityIgnoreCase(String city);

    // Сортировка
    List<Publisher> findAllByOrderByNameAsc();
    List<Publisher> findAllByOrderByNameDesc();
    List<Publisher> findAllByOrderByCityAsc();

    // Проверка наличия книг у издательства
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.publisher.id = :publisherId")
    boolean hasBooks(@Param("publisherId") Long publisherId);

    // Количество книг у издательства
    @Query("SELECT COUNT(b) FROM Book b WHERE b.publisher.id = :publisherId")
    long countBooks(@Param("publisherId") Long publisherId);
}

