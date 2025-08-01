package com.okbasalman.api_gateway.dto.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreateDto {
    @JsonProperty("user_id")
    private String userId;
    private String email;
    private String address;
    private List<OrderItemDto> items;
    
}
