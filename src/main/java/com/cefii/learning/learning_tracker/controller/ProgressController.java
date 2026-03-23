package com.cefii.learning.learning_tracker.controller;

import com.cefii.learning.learning_tracker.model.Progress;
import com.cefii.learning.learning_tracker.service.ProgressService;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Progress>> getAllProgresses() {
        List<Progress> progresses = progressService.getAllProgresses();
        if (progresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(@PathVariable Long id) {
        Progress progress = progressService.getProgressById(id);
        if (progress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Progress>> getProgressesByUser(@PathVariable Long user_Id) {
        List<Progress> progresses = progressService.getProgressesByUser(user_Id);
        if (progresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Progress>> getProgressionsByCourse(@PathVariable Long course_Id) {
        List<Progress> progresses = progressService.getProgressionsByCourse(course_Id);
        if (progresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<Progress> getProgressByUserAndCourse(@PathVariable Long user_Id,
            @PathVariable Long course_Id) {
        Progress progress = progressService.getProgressByUserAndCourse(user_Id, course_Id);
        if (progress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<Double> getAverageProgressForCourse(@PathVariable Long course_Id) {
        Double average = progressService.getAverageProgressForCourse(course_Id);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getAverageProgressForUser(@PathVariable Long user_Id) {
        Double average = progressService.getAverageProgressForUser(user_Id);
        return ResponseEntity.ok(average);
    }

    @PostMapping("/add")
    public ResponseEntity<Progress> createProgress(@RequestBody Progress progress) {
        Progress createdProgress = progressService.createProgress(progress);
        if (createdProgress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(createdProgress);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Progress> updateProgress(@PathVariable Long id, @RequestBody Integer percentage) {
        Progress updatedProgress = progressService.updateProgress(id, percentage);
        if (updatedProgress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedProgress);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }
}
