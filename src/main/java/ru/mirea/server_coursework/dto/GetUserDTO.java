package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Описание класса
 */
@Getter
@Builder
public class GetUserDTO {

    private Long id;

    private String username;

    private String name;

    private String surname;

    private String phone;

    private String city;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate registrationDate;

    private float rating;

}
