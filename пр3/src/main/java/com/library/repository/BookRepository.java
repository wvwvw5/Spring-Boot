package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Поиск по названию
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Поиск по автору
    List<Book> findByAuthorId(Long authorId);

    // Поиск по издательству
    List<Book> findByPublisherId(Long publisherId);

    // Поиск по жанру
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreId")
    List<Book> findByGenreId(@Param("genreId") Long genreId);

    // Комбинированный поиск
    @Query("SELECT DISTINCT b FROM Book b " +
           "LEFT JOIN b.author a " +
           "LEFT JOIN b.publisher p " +
           "LEFT JOIN b.genres g " +
           "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:authorId IS NULL OR a.id = :authorId) " +
           "AND (:publisherId IS NULL OR p.id = :publisherId) " +
           "AND (:genreId IS NULL OR g.id = :genreId) " +
           "AND (:yearFrom IS NULL OR b.publicationYear >= :yearFrom) " +
           "AND (:yearTo IS NULL OR b.publicationYear <= :yearTo)")
    List<Book> findByFilters(@Param("title") String title,
                             @Param("authorId") Long authorId,
                             @Param("publisherId") Long publisherId,
                             @Param("genreId") Long genreId,
                             @Param("yearFrom") Integer yearFrom,
                             @Param("yearTo") Integer yearTo);

    // Сортировка
    List<Book> findAllByOrderByTitleAsc();
    List<Book> findAllByOrderByTitleDesc();
    List<Book> findAllByOrderByPublicationYearAsc();
    List<Book> findAllByOrderByPublicationYearDesc();
    List<Book> findAllByOrderByPriceAsc();
    List<Book> findAllByOrderByPriceDesc();

    // Доступные книги
    List<Book> findByAvailableCopiesGreaterThan(Integer copies);

    // Проверка наличия активных выдач
    @Query("SELECT COUNT(l) > 0 FROM BookLoan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    boolean hasActiveLoans(@Param("bookId") Long bookId);

    // Количество активных выдач
    @Query("SELECT COUNT(l) FROM BookLoan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    long countActiveLoans(@Param("bookId") Long bookId);
}


