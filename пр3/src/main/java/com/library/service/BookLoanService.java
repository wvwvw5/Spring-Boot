package com.library.service;

import com.library.model.Book;
import com.library.model.BookLoan;
import com.library.repository.BookLoanRepository;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookLoanService(BookLoanRepository bookLoanRepository, BookRepository bookRepository) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
    }

    public BookLoan save(BookLoan loan) {
        // Уменьшаем количество доступных копий
        Book book = loan.getBook();
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);
        }
        return bookLoanRepository.save(loan);
    }

    public List<BookLoan> findAll() {
        return bookLoanRepository.findAll();
    }

    public Optional<BookLoan> findById(Long id) {
        return bookLoanRepository.findById(id);
    }

    public BookLoan update(Long id, BookLoan loanDetails) {
        BookLoan loan = bookLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));
        loan.setReaderName(loanDetails.getReaderName());
        loan.setReaderEmail(loanDetails.getReaderEmail());
        loan.setReaderPhone(loanDetails.getReaderPhone());
        loan.setDueDate(loanDetails.getDueDate());
        loan.setNotes(loanDetails.getNotes());
        return bookLoanRepository.save(loan);
    }

    public BookLoan returnBook(Long id) {
        BookLoan loan = bookLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));
        loan.setStatus(BookLoan.LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        
        // Увеличиваем количество доступных копий
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        
        return bookLoanRepository.save(loan);
    }

    public void delete(Long id) {
        BookLoan loan = bookLoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));
        
        // Если выдача активна, возвращаем книгу
        if (loan.getStatus() == BookLoan.LoanStatus.ACTIVE) {
            Book book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }
        
        bookLoanRepository.deleteById(id);
    }

    public void deleteMultiple(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    public List<BookLoan> findByFilters(String readerName, Long bookId, BookLoan.LoanStatus status) {
        return bookLoanRepository.findByFilters(readerName, bookId, status);
    }

    public List<BookLoan> findOverdue() {
        return bookLoanRepository.findOverdue(LocalDate.now());
    }

    public List<BookLoan> findActive() {
        return bookLoanRepository.findByStatus(BookLoan.LoanStatus.ACTIVE);
    }

    public List<BookLoan> findAllSorted(String sortBy) {
        switch (sortBy != null ? sortBy : "loanDate") {
            case "dueDate":
                return bookLoanRepository.findAllByOrderByDueDateAsc();
            case "readerName":
                return bookLoanRepository.findAllByOrderByReaderNameAsc();
            default:
                return bookLoanRepository.findAllByOrderByLoanDateDesc();
        }
    }
}


