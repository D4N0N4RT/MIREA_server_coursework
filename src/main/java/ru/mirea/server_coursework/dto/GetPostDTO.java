package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.mirea.server_coursework.model.Category;

import java.time.LocalDate;

@Getter
@Builder
public class GetPostDTO {
    private long id;

    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Category category;

    private double price;

    private String userEmail;

    private Float sellerRating;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate postingDate;

    private String description;

    private boolean exchanged;

    private boolean delivered;
}
