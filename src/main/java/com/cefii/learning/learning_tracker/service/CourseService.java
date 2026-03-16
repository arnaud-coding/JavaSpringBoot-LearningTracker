package com.cefii.learning.learning_tracker.service;

import com.cefii.learning.learning_tracker.repository.CourseRepository;
import com.cefii.learning.learning_tracker.model.Course;
import org.springframework.stereotype.Service;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;  //* unnecessary since Spring 4.3 if the class has only one constructor

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    // Constructor-based dependency injection
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course getCourseById(Long id_course) {
        return courseRepository.findById(id_course).orElse(null);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // ----------------- Admin's operations -----------------
    public Course createCourse(Course course) {
        // Todo: add admin's rights check
        return courseRepository.save(course);
    }

    // -----------------
    public Course updateCourse(Long id_course, Course course) {
        // Todo: add admin's rights check
        Course existingCourse = courseRepository.findById(id_course).orElse(null);
        if (existingCourse == null)
            return null; // Or throw an exception

        existingCourse.setTitle(course.getTitle());
        existingCourse.setDescription(course.getDescription());

        return courseRepository.save(existingCourse);
    }

    // -----------------
    public void deleteCourse(Long id_course) {
        // Todo: add admin's rights check
        // Todo: add transactional deletion of all related content and progress records
        courseRepository.deleteById(id_course);
    }
}
