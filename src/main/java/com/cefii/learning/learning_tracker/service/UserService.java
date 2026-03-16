package com.cefii.learning.learning_tracker.service;

import com.cefii.learning.learning_tracker.repository.UserRepository;
import com.cefii.learning.learning_tracker.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired; //* unnecessary since Spring 4.3 if the class has only one constructor

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor-based dependency injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id_user) {
        return userRepository.findById(id_user).orElse(null);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ----------------- Admin's operations -----------------
    public User createUser(User user) {
        // Todo: add admin's rights check

        // Checks if the username is already taken by another user
        List<String> allUsernames = userRepository.findAllusernames();
        if (allUsernames.contains(user.getUsername()))
            return null; // Or throw an exception

        // Todo: add password hashing

        return userRepository.save(user);
    }

    // -----------------
    public User updateUser(Long id_user, User user) {
        // Todo: add admin's rights check
        User existingUser = this.getUserById(id_user);
        if (existingUser == null)
            return null; // Or throw an exception

        // Checks if the username is already taken by another user
        List<String> allUsernames = userRepository.findAllusernames();
        if (allUsernames.contains(user.getUsername()))
            return null; // Or throw an exception

        // Todo: add password hashing
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

    // -----------------
    public void deleteUser(Long id_user) {
        // Todo: add admin's rights check
        // Todo: add transactional deletion of all the user's progress records
        userRepository.deleteById(id_user);
    }

}
