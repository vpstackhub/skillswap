package com.skillswap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each booking belongs to one student (User)
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // Each booking belongs to one teacher (User)
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    private LocalDateTime dateTime;

    private String topic;

    private String status; // e.g., PENDING, CONFIRMED, CANCELLED

    // Constructors
    public Booking() {}

    public Booking(User student, User teacher, LocalDateTime dateTime, String topic, String status) {
        this.student = student;
        this.teacher = teacher;
        this.dateTime = dateTime;
        this.topic = topic;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking [id=" + id + ", student=" + student + ", teacher=" + teacher + ", dateTime=" + dateTime
                + ", topic=" + topic + ", status=" + status + "]";
    }

}
