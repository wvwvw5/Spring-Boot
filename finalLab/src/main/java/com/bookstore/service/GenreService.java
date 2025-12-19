package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Genre;
import com.bookstore.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreService {
    
    @Autowired
    private GenreRepository genreRepository;
    
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
    
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с ID " + id + " не найден"));
    }
    
    public List<Genre> searchGenres(String name) {
        return genreRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Genre createGenre(Genre genre) {
        if (genre.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании жанра");
        }
        return genreRepository.save(genre);
    }
    
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);
        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());
        return genreRepository.save(genre);
    }
    
    public void deleteGenre(Long id) {
        Genre genre = getGenreById(id);
        if (!genre.getBooks().isEmpty()) {
            throw new ValidationException("Невозможно удалить жанр с существующими книгами");
        }
        genreRepository.delete(genre);
    }
}


