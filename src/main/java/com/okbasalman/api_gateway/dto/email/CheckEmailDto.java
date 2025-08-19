package com.okbasalman.api_gateway.dto.email;

import lombok.Data;

@Data
public class CheckEmailDto {
    private String email;
    private String code;
}
