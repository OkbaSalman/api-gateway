package com.okbasalman.api_gateway.dto.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CreateOrderByAdminDto {
    private String email;
    private String user_id;
    private String address;
    private List<OrderItemDto> items;
}
