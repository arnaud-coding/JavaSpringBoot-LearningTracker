package com.cefii.learning.learning_tracker.controller;

import com.cefii.learning.learning_tracker.model.Course;
import com.cefii.learning.learning_tracker.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    // @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(course);
    }

    @PostMapping("/add")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        if (createdCourse == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(createdCourse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        if (updatedCourse == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
