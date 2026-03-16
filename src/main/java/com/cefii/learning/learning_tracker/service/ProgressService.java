package com.cefii.learning.learning_tracker.service;

import com.cefii.learning.learning_tracker.repository.ProgressRepository;
import org.springframework.stereotype.Service;
import com.cefii.learning.learning_tracker.model.Progress;
import com.cefii.learning.learning_tracker.model.Course;
import com.cefii.learning.learning_tracker.model.User;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired; //* unnecessary since Spring 4.3 if the class has only one constructor

@Service
public class ProgressService {

    private ProgressRepository progressRepository;

    // Constructor-based dependency injection
    public ProgressService(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public Progress getProgressById(Long id_progress) {
        return progressRepository.findById(id_progress).orElse(null);
    }

    public Progress getProgressByUserAndCourse(User user, Course course) {
        return progressRepository.findByUserIdAndCourseId(user.getId_user(), course.getId_course());
    }

    public List<Progress> getAllProgresses() {
        return progressRepository.findAll();
    }

    public List<Progress> getProgressesByUser(User user) {
        return progressRepository.findAllByUserId(user.getId_user());
    }

    public List<Progress> getProgressionsByCourse(Course course) {
        return progressRepository.findAllByCourseId(course.getId_course());
    }

    public Double getAverageProgressForCourse(Course course) {
        return progressRepository.getAverageProgressForCourse(course.getId_course());
    }

    public Double getAverageProgressForUser(User user) {
        return progressRepository.getAverageProgressForUser(user.getId_user());
    }

    // ----------------- Admin's operations -----------------
    public Progress createProgress(Progress progress) {
        // Todo: add amin's rights check
        return progressRepository.save(progress);
    }

    public Progress updateProgress(Long id_progress, int percentage) {
        // Todo: add amin's rights check
        Progress progress = progressRepository.findById(id_progress).orElse(null);
        if (progress == null)
            return null; // Or throw an exception

        progress.setPercentage(percentage);

        return progressRepository.save(progress);
    }

    public void deleteProgress(Long id_progress) {
        // Todo: add amin's rights check
        progressRepository.deleteById(id_progress);
    }
}
