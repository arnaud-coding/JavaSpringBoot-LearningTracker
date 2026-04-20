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

    public List<Progress> getProgressesByUser(Long id_user) {
        return progressRepository.findAllByUserId(id_user);
    }

    public List<Progress> getProgressionsByCourse(Long id_course) {
        return progressRepository.findAllByCourseId(id_course);
    }

    public Progress getProgressByUserAndCourse(Long id_user, Long id_course) {
        return progressRepository.findByUserIdAndCourseId(id_user, id_course);
    }

    public Double getAverageProgressForCourse(Long id_course) {
        return progressRepository.getAverageProgressForCourse(id_course);
    }

    public Double getAverageProgressForUser(Long id_user) {
        return progressRepository.getAverageProgressForUser(id_user);
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
