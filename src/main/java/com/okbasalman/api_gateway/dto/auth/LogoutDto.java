package com.okbasalman.api_gateway.dto.auth;

import lombok.Data;

@Data
public class LogoutDto {
    private String refreshToken;
}
