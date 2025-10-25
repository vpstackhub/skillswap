package com.skillswap.controller;

import com.skillswap.model.Review;
import com.skillswap.repository.BookingRepository;
import com.skillswap.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookingRepository bookingRepository; // ✅ add this

    public ReviewController(ReviewService reviewService, BookingRepository bookingRepository) {
        this.reviewService = reviewService;
        this.bookingRepository = bookingRepository;
    }

    // ✅ Create a review (auto-populate sessionDate if classId is known)
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            // Ensure createdAt exists
            if (review.getCreatedAt() == null) {
                review.setCreatedAt(LocalDateTime.now());
            }

            // ✅ If classId provided, attach the sessionDate from that Booking
            if (review.getClassId() != null) {
                bookingRepository.findById(review.getClassId()).ifPresent(booking -> {
                    review.setSessionDate(booking.getDateTime());
                });
            }

            System.out.println("Received review for class: "
                    + review.getClassName()
                    + " (ID: " + review.getClassId() + ") "
                    + " Session Date: " + review.getSessionDate());

            Review saved = reviewService.createReview(review);
            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //  Get all reviews for a specific teacher
    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<Review>> getReviewsByTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsByTeacher(id));
    }

    //  Get all reviews for a specific class
    @GetMapping("/class/{id}")
    public ResponseEntity<List<Review>> getReviewsByClass(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsByClass(id));
    }

    //  Get average rating for a teacher
    @GetMapping("/teacher/{id}/average")
    public ResponseEntity<Double> getAverageRatingForTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getAverageRatingForTeacher(id));
    }

    //  Get average rating for a class
    @GetMapping("/class/{id}/average")
    public ResponseEntity<Double> getAverageRatingForClass(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getAverageRatingForClass(id));
    }
}
