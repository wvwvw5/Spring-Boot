package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Publisher;
import com.bookstore.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublisherService {
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }
    
    public Publisher getPublisherById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с ID " + id + " не найдено"));
    }
    
    public List<Publisher> searchPublishers(String name) {
        return publisherRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Publisher createPublisher(Publisher publisher) {
        if (publisher.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании издательства");
        }
        return publisherRepository.save(publisher);
    }
    
    public Publisher updatePublisher(Long id, Publisher publisherDetails) {
        Publisher publisher = getPublisherById(id);
        publisher.setName(publisherDetails.getName());
        publisher.setAddress(publisherDetails.getAddress());
        publisher.setPhone(publisherDetails.getPhone());
        publisher.setEmail(publisherDetails.getEmail());
        return publisherRepository.save(publisher);
    }
    
    public void deletePublisher(Long id) {
        Publisher publisher = getPublisherById(id);
        if (!publisher.getBooks().isEmpty()) {
            throw new ValidationException("Невозможно удалить издательство с существующими книгами");
        }
        publisherRepository.delete(publisher);
    }
}


