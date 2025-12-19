package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Author;
import com.bookstore.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автор с ID " + id + " не найден"));
    }
    
    public List<Author> searchAuthors(String name) {
        return authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
    
    public Author createAuthor(Author author) {
        if (author.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании автора");
        }
        return authorRepository.save(author);
    }
    
    public Author updateAuthor(Long id, Author authorDetails) {
        Author author = getAuthorById(id);
        author.setFirstName(authorDetails.getFirstName());
        author.setLastName(authorDetails.getLastName());
        author.setBiography(authorDetails.getBiography());
        return authorRepository.save(author);
    }
    
    public void deleteAuthor(Long id) {
        Author author = getAuthorById(id);
        if (!author.getBooks().isEmpty()) {
            throw new ValidationException("Невозможно удалить автора с существующими книгами");
        }
        authorRepository.delete(author);
    }
}


