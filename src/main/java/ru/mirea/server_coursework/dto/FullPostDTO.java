package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.mirea.server_coursework.model.Category;

import java.time.LocalDate;

/**
 * Описание класса
 */
@Getter
@Builder
public class FullPostDTO {

    private long id;

    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Category category;

    private double price;

    //@JsonFormat(shape = JsonFormat.Shape.STRING)
    private GetUserDTO seller;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate postingDate;

    private String description;

    private String city;

    private boolean exchanged;

    private boolean delivered;

    private boolean sold;

    private Long buyerId;
}
