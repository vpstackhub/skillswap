package com.skillswap.controller;

import com.skillswap.model.ClassEntity;
import com.skillswap.service.ClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "http://localhost:4200")  
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public List<ClassEntity> getAllClasses() {
        return classService.getAllClasses();
    }

    @GetMapping("/{id}")
    public ClassEntity getClassById(@PathVariable Long id) {
        return classService.getClassById(id).orElse(null);
    }

    @PostMapping
    public ClassEntity createClass(@RequestBody ClassEntity classEntity) {
        return classService.saveClass(classEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classService.deleteClass(id);
    }
}