package com.okbasalman.api_gateway.dto.auth;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
}
