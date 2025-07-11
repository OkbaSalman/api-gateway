package com.okbasalman.api_gateway.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCreateDto {
    private String name;
    private double price;
    private int stock;
}
