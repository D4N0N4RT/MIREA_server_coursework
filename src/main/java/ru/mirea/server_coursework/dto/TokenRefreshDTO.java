package ru.mirea.server_coursework.dto;

import javax.validation.constraints.NotBlank;

public class TokenRefreshDTO {
    @NotBlank
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
