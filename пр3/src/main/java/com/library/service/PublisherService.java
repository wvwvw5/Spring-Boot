package com.library.service;

import com.library.model.Publisher;
import com.library.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Optional<Publisher> findById(Long id) {
        return publisherRepository.findById(id);
    }

    public Publisher update(Long id, Publisher publisherDetails) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
        publisher.setName(publisherDetails.getName());
        publisher.setAddress(publisherDetails.getAddress());
        publisher.setCity(publisherDetails.getCity());
        publisher.setCountry(publisherDetails.getCountry());
        publisher.setEmail(publisherDetails.getEmail());
        publisher.setPhone(publisherDetails.getPhone());
        return publisherRepository.save(publisher);
    }

    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }

    public boolean hasBooks(Long id) {
        return publisherRepository.hasBooks(id);
    }

    public long countBooks(Long id) {
        return publisherRepository.countBooks(id);
    }

    public void deleteMultiple(List<Long> ids) {
        publisherRepository.deleteAllById(ids);
    }

    public List<Publisher> search(String query) {
        if (query == null || query.isEmpty()) {
            return findAll();
        }
        return publisherRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Publisher> findAllSorted(String sortBy, String sortDir) {
        if ("city".equals(sortBy)) {
            return publisherRepository.findAllByOrderByCityAsc();
        } else {
            return "desc".equals(sortDir)
                    ? publisherRepository.findAllByOrderByNameDesc()
                    : publisherRepository.findAllByOrderByNameAsc();
        }
    }
}


