package com.okbasalman.api_gateway.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteProductResultDto {
    private boolean success;
    private String message;
}
