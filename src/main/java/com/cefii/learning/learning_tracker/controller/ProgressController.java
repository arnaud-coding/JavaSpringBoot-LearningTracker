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

    @GetMapping("/user/{id_user}")
    public ResponseEntity<List<Progress>> getProgressesByUser(@PathVariable Long id_user) {
        List<Progress> progresses = progressService.getProgressesByUser(id_user);
        if (progresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/course/{id_course}")
    public ResponseEntity<List<Progress>> getProgressionsByCourse(@PathVariable Long id_course) {
        List<Progress> progresses = progressService.getProgressionsByCourse(id_course);
        if (progresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(progresses);
    }

    @GetMapping("/user/{id_user}/course/{id_course}")
    public ResponseEntity<Progress> getProgressByUserAndCourse(@PathVariable Long id_user,
            @PathVariable Long id_course) {
        Progress progress = progressService.getProgressByUserAndCourse(id_user, id_course);
        if (progress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/course/{id_course}/average")
    public ResponseEntity<Double> getAverageProgressForCourse(@PathVariable Long id_course) {
        Double average = progressService.getAverageProgressForCourse(id_course);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/user/{id_user}/average")
    public ResponseEntity<Double> getAverageProgressForUser(@PathVariable Long id_user) {
        Double average = progressService.getAverageProgressForUser(id_user);
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
