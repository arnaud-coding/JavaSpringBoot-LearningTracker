package com.cefii.learning.learning_tracker.repository;

import com.cefii.learning.learning_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    @Query("SELECT u.username FROM User u")
    public List<String> findAllusernames();
}