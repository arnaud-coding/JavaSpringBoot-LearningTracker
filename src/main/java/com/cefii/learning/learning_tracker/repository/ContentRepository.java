package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
