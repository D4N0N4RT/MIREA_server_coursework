package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Описание класса
 */
@Getter
@Builder
public class ReviewDTO {

    private Long id;

    private GetUserDTO reviewAuthor;

    private ShortPostDTO post;

    private GetUserDTO postAuthor;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd.MM.yyyy")
    private LocalDateTime time;

    private int grade;
}
