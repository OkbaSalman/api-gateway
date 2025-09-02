package com.okbasalman.api_gateway.dto.order;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderCreateDto {
    private String address;
    private List<OrderItemDto> items;
    
}
