package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
}