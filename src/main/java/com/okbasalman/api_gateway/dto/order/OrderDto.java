package com.okbasalman.api_gateway.dto.order;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDto {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("user_id")
    private String userId;
    private String status;
    private List<OrderItemDto> items;
    private Long createdAt;
    
}
