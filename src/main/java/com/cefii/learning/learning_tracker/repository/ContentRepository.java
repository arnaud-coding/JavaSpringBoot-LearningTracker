package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT c FROM Content c WHERE c.id_course = :id_course")
    public List<Content> findAllByCourseId(@Param("id_course") Long id_course);
}
