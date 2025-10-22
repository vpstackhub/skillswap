package com.skillswap.controller;

import com.skillswap.model.Booking;
import com.skillswap.model.User;
import com.skillswap.repository.UserRepository;
import com.skillswap.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    //  Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.saveBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    //  Get all bookings
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    //  Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Get bookings for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Booking>> getBookingsByStudent(@PathVariable Long studentId) {
        Optional<User> student = userRepository.findById(studentId);
        return student.map(value -> ResponseEntity.ok(bookingService.getBookingsByStudent(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Get bookings for a teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Booking>> getBookingsByTeacher(@PathVariable Long teacherId) {
        Optional<User> teacher = userRepository.findById(teacherId);
        return teacher.map(value -> ResponseEntity.ok(bookingService.getBookingsByTeacher(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Update booking (e.g., confirm or cancel)
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody Booking updatedBooking) {
        try {
            Booking saved = bookingService.updateBooking(id, updatedBooking);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
