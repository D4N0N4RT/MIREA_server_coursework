package ru.mirea.server_coursework.dto;

import lombok.Getter;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Описание класса
 */
@Getter
public class CreateReviewDTO {
    @NotBlank(message = "Отзыв не может быть пустым")
    private String content;

    @Size(min = 1, max = 5, message = "Оценка должна быть целым числом от 1 до 5")
    private int grade;

    public Review toReview(Post post, User user) {
        return Review.builder()
                .author(user)
                .post(post)
                .content(content)
                .grade(grade)
                .time(LocalDateTime.now())
                .build();
    }
}
