package com.cefii.learning.learning_tracker.service;

import com.cefii.learning.learning_tracker.repository.ProgressRepository;
import org.springframework.stereotype.Service;
import com.cefii.learning.learning_tracker.model.Progress;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired; //* unnecessary since Spring 4.3 if the class has only one constructor

@Service
public class ProgressService {

    private ProgressRepository progressRepository;

    // Constructor-based dependency injection
    public ProgressService(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public List<Progress> getAllProgresses() {
        return progressRepository.findAll();
    }

    public Progress getProgressById(Long id_progress) {
        return progressRepository.findById(id_progress).orElse(null);
    }

    public List<Progress> getProgressesByUser(Long user_id) {
        return progressRepository.findAllByUserId(user_id);
    }

    public List<Progress> getProgressionsByCourse(Long course_id) {
        return progressRepository.findAllByCourseId(course_id);
    }

    public Progress getProgressByUserAndCourse(Long user_id, Long course_id) {
        return progressRepository.findByUserIdAndCourseId(user_id, course_id);
    }

    public Double getAverageProgressForCourse(Long course_id) {
        return progressRepository.getAverageProgressForCourse(course_id);
    }

    public Double getAverageProgressForUser(Long user_id) {
        return progressRepository.getAverageProgressForUser(user_id);
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
