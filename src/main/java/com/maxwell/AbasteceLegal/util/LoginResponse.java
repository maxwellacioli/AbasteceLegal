package com.maxwell.AbasteceLegal.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    private String token;

    private final String type = "Bearer";

    private Long id;

    private String username;

    private String name;

    private String email;

    public LoginResponse(String token, Long id, String username,
                            String name, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

}
