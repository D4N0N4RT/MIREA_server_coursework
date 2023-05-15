package ru.mirea.server_coursework.dto;

import lombok.Getter;

@Getter
public class TokenRefreshResponseDTO {
    private String accessToken;
    private String refreshToken;

    public TokenRefreshResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
