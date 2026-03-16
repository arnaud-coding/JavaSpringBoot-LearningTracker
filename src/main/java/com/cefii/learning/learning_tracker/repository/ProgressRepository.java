package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query("SELECT p FROM Progress p WHERE p.id_user = :id_user AND p.id_course = :id_course")
    Progress findByUserIdAndCourseId(@Param("id_user") Long id_user, @Param("id_course") Long id_course);

    @Query("SELECT AVG(p.percentage) FROM Progress p WHERE p.id_course = :id_course")
    Double getAverageProgressForCourse(@Param("id_course") Long id_course);

    @Query("SELECT AVG(p.percentage) FROM Progress p WHERE p.id_user = :id_user")
    Double getAverageProgressForUser(@Param("id_user") Long id_user);

    @Query("SELECT p FROM Progress p WHERE p.username = :username")
    List<Progress> findAllByUserName(@Param("username") String username);

    @Query("SELECT p FROM Progress p WHERE p.id_user = :id_user")
    List<Progress> findAllByUserId(@Param("id_user") Long id_user);

    @Query("SELECT p FROM Progress p WHERE p.id_course = :id_course")
    List<Progress> findAllByCourseId(@Param("id_course") Long id_course);

}
