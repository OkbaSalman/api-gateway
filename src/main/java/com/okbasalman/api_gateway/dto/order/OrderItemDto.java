package com.okbasalman.api_gateway.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    @JsonProperty("product_id")
    private Long productId;
    private int quantity;
    
}

