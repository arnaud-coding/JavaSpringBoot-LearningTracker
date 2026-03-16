package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}