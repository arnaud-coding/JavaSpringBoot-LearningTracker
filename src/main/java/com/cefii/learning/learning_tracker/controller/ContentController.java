package com.cefii.learning.learning_tracker.controller;

import com.cefii.learning.learning_tracker.model.Content;
import com.cefii.learning.learning_tracker.model.User;
import com.cefii.learning.learning_tracker.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/contents")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Content>> getAllContents() {
        List<Content> contents = contentService.getAllContents();
        if (contents.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable Long id) {
        Content content = contentService.getContentById(id);
        if (content == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(content);
    }

    @GetMapping("/course/{id_course}")
    public ResponseEntity<List<Content>> getAllContentsByCourseId(@PathVariable Long id_course) {
        List<Content> contents = contentService.getAllContentsByCourseId(id_course);
        if (contents.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(contents);
    }

    @PostMapping("/add")
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        Content createdContent = contentService.createContent(content);
        if (createdContent == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable Long id, @RequestBody Content content) {
        Content updatedContent = contentService.updateContent(id, content);
        if (updatedContent == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedContent);
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<Content> markContentAsRead(@PathVariable Long id, @RequestBody User user) {
        Content content = contentService.markContentAsRead(id, user);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
