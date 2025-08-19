package com.okbasalman.api_gateway.dto.email;

import lombok.Data;

@Data
public class SendEmailDto {
    private String email;
    private String username;

}
