package com.skillswap.repository;

import com.skillswap.model.Booking;
import com.skillswap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings by student
    List<Booking> findByStudent(User student);

    // Find all bookings by teacher
    List<Booking> findByTeacher(User teacher);
}
