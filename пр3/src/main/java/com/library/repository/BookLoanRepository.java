package com.library.repository;

import com.library.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    // Поиск по книге
    List<BookLoan> findByBookId(Long bookId);

    // Поиск по имени читателя
    List<BookLoan> findByReaderNameContainingIgnoreCase(String readerName);

    // Поиск по статусу
    List<BookLoan> findByStatus(BookLoan.LoanStatus status);

    // Активные выдачи
    List<BookLoan> findByStatusOrderByDueDateAsc(BookLoan.LoanStatus status);

    // Просроченные выдачи
    @Query("SELECT l FROM BookLoan l WHERE l.status = 'ACTIVE' AND l.dueDate < :today")
    List<BookLoan> findOverdue(@Param("today") LocalDate today);

    // Комбинированный поиск
    @Query("SELECT l FROM BookLoan l " +
           "WHERE (:readerName IS NULL OR LOWER(l.readerName) LIKE LOWER(CONCAT('%', :readerName, '%'))) " +
           "AND (:bookId IS NULL OR l.book.id = :bookId) " +
           "AND (:status IS NULL OR l.status = :status)")
    List<BookLoan> findByFilters(@Param("readerName") String readerName,
                                  @Param("bookId") Long bookId,
                                  @Param("status") BookLoan.LoanStatus status);

    // Сортировка
    List<BookLoan> findAllByOrderByLoanDateDesc();
    List<BookLoan> findAllByOrderByDueDateAsc();
    List<BookLoan> findAllByOrderByReaderNameAsc();
}

