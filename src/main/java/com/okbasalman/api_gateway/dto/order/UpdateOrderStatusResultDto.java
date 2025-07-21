package com.okbasalman.api_gateway.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateOrderStatusResultDto {
    private boolean success;
    private String message;
}
