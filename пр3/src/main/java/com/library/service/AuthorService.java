package com.library.service;

import com.library.model.Author;
import com.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public Author update(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        author.setFirstName(authorDetails.getFirstName());
        author.setLastName(authorDetails.getLastName());
        author.setBirthDate(authorDetails.getBirthDate());
        author.setCountry(authorDetails.getCountry());
        author.setBiography(authorDetails.getBiography());
        return authorRepository.save(author);
    }

    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    public boolean hasBooks(Long id) {
        return authorRepository.hasBooks(id);
    }

    public long countBooks(Long id) {
        return authorRepository.countBooks(id);
    }

    public void deleteMultiple(List<Long> ids) {
        authorRepository.deleteAllById(ids);
    }

    public List<Author> search(String query) {
        if (query == null || query.isEmpty()) {
            return findAll();
        }
        return authorRepository.searchByName(query);
    }

    public List<Author> findAllSorted(String sortBy, String sortDir) {
        if ("firstName".equals(sortBy)) {
            return authorRepository.findAllByOrderByFirstNameAsc();
        } else if ("birthDate".equals(sortBy)) {
            return authorRepository.findAllByOrderByBirthDateAsc();
        } else {
            return "desc".equals(sortDir)
                    ? authorRepository.findAllByOrderByLastNameDesc()
                    : authorRepository.findAllByOrderByLastNameAsc();
        }
    }
}

