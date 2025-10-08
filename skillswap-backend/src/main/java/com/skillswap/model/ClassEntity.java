package com.skillswap.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)  // allow longer text
    private String description;

    private String category;  // e.g. Cooking, Programming, Fitness
    private String teacher;   // later linkable to User entity
    private String schedule;  // simple string for now (e.g. "Mondays 6PM")

    // Constructors
    public ClassEntity() {}

    public ClassEntity(String title, String description, String category, String teacher, String schedule) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.teacher = teacher;
        this.schedule = schedule;
    }

    // Getters & setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
}
