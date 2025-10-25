package com.skillswap.service;

import com.skillswap.model.Review;
import com.skillswap.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // ✅ Create a new review
    public Review createReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        return reviewRepository.save(review);
    }

    //  Fetch reviews for a teacher
    public List<Review> getReviewsByTeacher(Long teacherId) {
        return reviewRepository.findByTeacherId(teacherId);
    }

    //  Fetch reviews for a class
    public List<Review> getReviewsByClass(Long classId) {
        return reviewRepository.findByClassId(classId);
    }

    //  Calculate average rating for teacher
    public double getAverageRatingForTeacher(Long teacherId) {
        List<Review> reviews = reviewRepository.findByTeacherId(teacherId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }

    //  Calculate average rating for class
    public double getAverageRatingForClass(Long classId) {
        List<Review> reviews = reviewRepository.findByClassId(classId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}
