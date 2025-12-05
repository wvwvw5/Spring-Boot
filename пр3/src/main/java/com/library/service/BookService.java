package com.library.service;

import com.library.model.Book;
import com.library.model.Genre;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book update(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setPages(bookDetails.getPages());
        book.setDescription(bookDetails.getDescription());
        book.setPrice(bookDetails.getPrice());
        book.setTotalCopies(bookDetails.getTotalCopies());
        book.setAvailableCopies(bookDetails.getAvailableCopies());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublisher(bookDetails.getPublisher());
        book.setGenres(bookDetails.getGenres());
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public boolean hasActiveLoans(Long id) {
        return bookRepository.hasActiveLoans(id);
    }

    public long countActiveLoans(Long id) {
        return bookRepository.countActiveLoans(id);
    }

    public void deleteMultiple(List<Long> ids) {
        bookRepository.deleteAllById(ids);
    }

    public List<Book> findByFilters(String title, Long authorId, Long publisherId, 
                                     Long genreId, Integer yearFrom, Integer yearTo) {
        return bookRepository.findByFilters(title, authorId, publisherId, genreId, yearFrom, yearTo);
    }

    public List<Book> findAllSorted(String sortBy, String sortDir) {
        boolean desc = "desc".equals(sortDir);
        switch (sortBy != null ? sortBy : "title") {
            case "year":
                return desc ? bookRepository.findAllByOrderByPublicationYearDesc()
                           : bookRepository.findAllByOrderByPublicationYearAsc();
            case "price":
                return desc ? bookRepository.findAllByOrderByPriceDesc()
                           : bookRepository.findAllByOrderByPriceAsc();
            default:
                return desc ? bookRepository.findAllByOrderByTitleDesc()
                           : bookRepository.findAllByOrderByTitleAsc();
        }
    }

    public List<Book> findAvailable() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }
}

