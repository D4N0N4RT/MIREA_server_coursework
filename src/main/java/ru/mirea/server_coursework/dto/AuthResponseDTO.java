package ru.mirea.server_coursework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    Long id;
    String username;
    String accessToken;
    String refreshToken;
    String role;
}
