package com.maxwell.AbasteceLegal.util;

import lombok.Getter;
import lombok.Setter;

public class LoginResponse {

    @Setter
    @Getter
    private String token;

    @Getter
    private final String type = "Bearer";

    @Setter
    @Getter
    private Long userId;

    @Setter
    @Getter
    private String username;

    public LoginResponse(String token, Long userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

}
