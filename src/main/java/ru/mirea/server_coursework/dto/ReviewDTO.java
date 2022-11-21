package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;

import java.time.LocalDateTime;

/**
 * Описание класса
 */
@Getter
@Builder
public class ReviewDTO {

    private String authorEmail;

    private Long postId;

    private String postAuthorEmail;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime time;

    private int grade;
}
