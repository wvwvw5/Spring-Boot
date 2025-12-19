package com.bookstore.service;

import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.exception.ValidationException;
import com.bookstore.model.Review;
import com.bookstore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookService bookService;
    
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
    
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв с ID " + id + " не найден"));
    }
    
    public List<Review> getReviewsByBook(Long bookId) {
        bookService.getBookById(bookId);
        return reviewRepository.findByBookIdOrderByReviewDateDesc(bookId);
    }
    
    public List<Review> getReviewsByUser(Long userId) {
        userService.getUserById(userId);
        return reviewRepository.findByUserId(userId);
    }
    
    public Review createReview(Review review) {
        if (review.getId() != null) {
            throw new ValidationException("ID должен быть пустым при создании отзыва");
        }
        review.setUser(userService.getUserById(review.getUser().getId()));
        review.setBook(bookService.getBookById(review.getBook().getId()));
        return reviewRepository.save(review);
    }
    
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = getReviewById(id);
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        return reviewRepository.save(review);
    }
    
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }
}


