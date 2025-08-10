package com.okbasalman.api_gateway.dto.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantCreateDto {
    private double price;
    private int stock;
    private String color;
    private String size;
    private List<String> base64Images;
}