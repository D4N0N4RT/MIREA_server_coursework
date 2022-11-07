package ru.mirea.server_coursework.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class AuthRequestDTO {
    @NotBlank(message = "Почта (имя пользователя) не может быть пустой")
    private String username;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
