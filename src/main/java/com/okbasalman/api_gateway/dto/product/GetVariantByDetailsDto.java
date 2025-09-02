package com.okbasalman.api_gateway.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetVariantByDetailsDto {
    private Long productId;
    private String size;
    private String color;
}