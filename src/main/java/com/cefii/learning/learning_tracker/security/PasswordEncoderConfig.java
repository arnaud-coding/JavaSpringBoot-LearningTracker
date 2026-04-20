package com.cefii.learning.learning_tracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class PasswordEncoderConfig {

    @Bean // Creates a Bean for the password encoder
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use Bcrypt to hash passwords

        // Code to verify the password (additional help ; do not implement here but in service or controller packages instead):
        // boolean isMatch = passwordEncoder.matches(rawPasswordFromLogin, encodedPasswordFromDb);
    }
}
