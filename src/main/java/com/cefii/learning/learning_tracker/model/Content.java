package com.cefii.learning.learning_tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "learning_tracker_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_content;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 250, message = "Title must be between 3 and 250 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "VIDEO|ARTICLE|QUIZ|PDF|EXERCISE", message = "Content type must be either VIDEO, ARTICLE, QUIZ, PDF, or EXERCISE")
    @Column(nullable = false)
    private String type;

    @NotBlank(message = "URL is required")
    @Size(min = 5, max = 2048, message = "URL must be between 5 and 2048 characters")
    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course", nullable = false)
    private Course id_course;
}
