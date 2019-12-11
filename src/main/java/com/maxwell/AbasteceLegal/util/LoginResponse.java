package com.maxwell.AbasteceLegal.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    private String token;

    private final String type = "Bearer";

    public LoginResponse(String token) {
        this.token = token;
    }
}
