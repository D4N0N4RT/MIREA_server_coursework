package ru.mirea.server_coursework.dto;

import lombok.Builder;
import lombok.Getter;
import ru.mirea.server_coursework.model.Role;
import ru.mirea.server_coursework.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class RegisterUserDTO implements IUserDTO {
    @NotBlank(message = "Почта (имя пользователя) не может быть пустой")
    @Size(max = 50, message = "Почта не может превышать 50 символов")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(max = 255, message = "Пароль не может превышать 255 символов")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не может превышать 50 символов")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 75, message = "Фамилия не может превышать 75 символов")
    private String surname;

    @NotBlank(message = "Номер телефона не может отсутствовать")
    @Size(min = 10, max = 10, message = "Номер телефона должен содержать 10 цифр")
    private String phone;

    @NotBlank(message = "Город не может отсутствовать")
    @Size(max = 50, message = "Название города должно содержать не больше 50 букв")
    private String city;

    public User toUser() {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .surname(surname)
                .phone(phone)
                .city(city)
                .isActive(true)
                .rating(0).role(Role.USER).build();
    }
}
