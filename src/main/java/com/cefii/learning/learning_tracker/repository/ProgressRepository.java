package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query("SELECT percentage FROM Progress WHERE Progress.id_user = :id_user AND Progress.id_course = :id_course")
    Progress findByUserIdAndCourseId(@Param("id_user") Long id_user, @Param("id_course") Long id_course);

    @Query("SELECT AVG(Progress.percentage) FROM Progress Progress WHERE Progress.id_course = :id_course")
    Double getAverageProgressForCourse(@Param("id_course") Long id_course);

    @Query("SELECT percentage FROM Progress Progress WHERE Progress.username = :username")
    List<Progress> findByStudentName(@Param("username") String username);

    @Query("SELECT percentage FROM Progress WHERE Progress.id_course = :id_course")
    List<Progress> findByCourseId(@Param("id_course") Long id_course);

}
