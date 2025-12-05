package com.library.service;

import com.library.model.Genre;
import com.library.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre update(Long id, Genre genreDetails) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Жанр не найден"));
        genre.setName(genreDetails.getName());
        genre.setDescription(genreDetails.getDescription());
        return genreRepository.save(genre);
    }

    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    public boolean hasBooks(Long id) {
        return genreRepository.hasBooks(id);
    }

    public long countBooks(Long id) {
        return genreRepository.countBooks(id);
    }

    public void deleteMultiple(List<Long> ids) {
        genreRepository.deleteAllById(ids);
    }

    public List<Genre> search(String query) {
        if (query == null || query.isEmpty()) {
            return findAll();
        }
        return genreRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Genre> findAllSorted(String sortDir) {
        return "desc".equals(sortDir)
                ? genreRepository.findAllByOrderByNameDesc()
                : genreRepository.findAllByOrderByNameAsc();
    }
}

