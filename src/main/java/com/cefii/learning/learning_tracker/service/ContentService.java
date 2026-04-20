package com.cefii.learning.learning_tracker.service;

import com.cefii.learning.learning_tracker.repository.ContentRepository;
import org.springframework.stereotype.Service;
import com.cefii.learning.learning_tracker.model.Content;
import com.cefii.learning.learning_tracker.model.User;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;  //* unnecessary since Spring 4.3 if the class has only one constructor

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    // Constructor-based dependency injection
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    public Content getContentById(Long id_content) {
        return contentRepository.findById(id_content).orElse(null);
    }

    public List<Content> getAllContentsByCourseId(Long id_course) {
        return contentRepository.findAllByCourseId(id_course);
    }

    // ----------------- Admin's operations -----------------
    public Content createContent(Content content) {
        // Todo: add admin's rights check
        return contentRepository.save(content);
    }

    // -----------------
    public Content updateContent(Long id_content, Content content) {
        // Todo: add admin's rights check
        Content existingContent = contentRepository.findById(id_content).orElse(null);
        if (existingContent == null)
            return null; // Or throw an exception

        existingContent.setTitle(content.getTitle());
        existingContent.setType(content.getType());
        existingContent.setUrl(content.getUrl());

        return contentRepository.save(existingContent);
    }

    // -----------------
    public void deleteContent(Long id_content) {
        // Todo: add admin's rights check
        contentRepository.deleteById(id_content);
    }

    // -----------------
    public Content markContentAsRead(Long id_content, User user) {
        // * Needs a specific logic to mark the content as read for the user
        // * To implement by following the reading tracking structure
        return getContentById(id_content);
    }
}
