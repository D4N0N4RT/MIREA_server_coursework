package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import ru.mirea.server_coursework.model.Category;

import java.time.LocalDate;

@Builder
public class GetPostDTO {
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Category category;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
    private double price;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String userEmail;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int rating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String description;
}
