package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private PublisherService publisherService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private GenreService genreService;
    
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с ID " + id + " не найдена"));
    }
    
    public List<Book> searchBooks(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Book> getBooksByPublisher(Long publisherId) {
        publisherService.getPublisherById(publisherId);
        return bookRepository.findByPublisherId(publisherId);
    }
    
    public List<Book> getBooksByGenre(Long genreId) {
        genreService.getGenreById(genreId);
        return bookRepository.findByGenresId(genreId);
    }
    
    public List<Book> getBooksByAuthor(Long authorId) {
        authorService.getAuthorById(authorId);
        return bookRepository.findByAuthorsId(authorId);
    }
    
    public Book createBook(Book book) {
        if (book.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании книги");
        }
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new ValidationException("Книга с ISBN " + book.getIsbn() + " уже существует");
        }
        if (book.getPublisher() != null && book.getPublisher().getId() != null) {
            book.setPublisher(publisherService.getPublisherById(book.getPublisher().getId()));
        }
        return bookRepository.save(book);
    }
    
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setPrice(bookDetails.getPrice());
        book.setPages(bookDetails.getPages());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setDescription(bookDetails.getDescription());
        if (bookDetails.getPublisher() != null && bookDetails.getPublisher().getId() != null) {
            book.setPublisher(publisherService.getPublisherById(bookDetails.getPublisher().getId()));
        }
        if (bookDetails.getAuthors() != null) {
            book.setAuthors(bookDetails.getAuthors());
        }
        if (bookDetails.getGenres() != null) {
            book.setGenres(bookDetails.getGenres());
        }
        return bookRepository.save(book);
    }
    
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        if (!book.getOrderItems().isEmpty()) {
            throw new ValidationException("Невозможно удалить книгу, которая присутствует в заказах");
        }
        bookRepository.delete(book);
    }
}


