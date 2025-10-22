package com.skillswap.service;

import com.skillswap.model.Booking;
import com.skillswap.model.User;
import com.skillswap.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService; 

    //  Constructor injection for both dependencies
    public BookingService(BookingRepository bookingRepository,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    // Create or update a booking
    public Booking saveBooking(Booking booking) {
        Booking saved = bookingRepository.save(booking);

        //  Notify both student and teacher after save
        try {
            if (saved.getStudent() != null && saved.getStudent().getEmail() != null) {
                notificationService.notifyStudent(
                    saved.getStudent().getEmail(),
                    "Your booking for '" + saved.getTopic() + "' has been created!"
                );
            }

            if (saved.getTeacher() != null && saved.getTeacher().getEmail() != null) {
                notificationService.notifyTeacher(
                    saved.getTeacher().getEmail(),
                    "New booking received from student " + saved.getStudent().getEmail()
                );
            }
        } catch (Exception e) {
            System.err.println("⚠️ Notification error: " + e.getMessage());
        }

        return saved; //  return the persisted booking
    }


    public Booking updateBooking(Long id, Booking updated) {
        return bookingRepository.findById(id).map(existing -> {
            // Prevent overwriting with nulls
            if (updated.getTopic() == null) {
                updated.setTopic(existing.getTopic());
            }

            existing.setDateTime(updated.getDateTime());
            existing.setTopic(updated.getTopic());
            existing.setStatus(updated.getStatus());
            Booking saved = bookingRepository.save(existing);

            // Notify both sides
    try {
        if ("CONFIRMED".equalsIgnoreCase(saved.getStatus())) {
            notificationService.notifyStudent(saved.getStudent().getEmail(),
                "Your booking for '" + saved.getTopic() + "' has been confirmed!");
            notificationService.notifyTeacher(saved.getTeacher().getEmail(),
                "Booking with student " + saved.getStudent().getEmail() + " confirmed.");
        } else if ("CANCELLED".equalsIgnoreCase(saved.getStatus())) {
            notificationService.notifyStudent(saved.getStudent().getEmail(),
                "Your booking for '" + saved.getTopic() + "' has been cancelled.");
            notificationService.notifyTeacher(saved.getTeacher().getEmail(),
                "Booking with student " + saved.getStudent().getEmail() + " was cancelled.");
        }
    } catch (Exception e) {
        System.err.println("⚠️ Notification error (update): " + e.getMessage());
    }
            return saved;
        }).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    // Find booking by ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // All bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Bookings for a student
    public List<Booking> getBookingsByStudent(User student) {
        return bookingRepository.findByStudent(student);
    }

    // Bookings for a teacher
    public List<Booking> getBookingsByTeacher(User teacher) {
        return bookingRepository.findByTeacher(teacher);
    }

    // Delete booking
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
