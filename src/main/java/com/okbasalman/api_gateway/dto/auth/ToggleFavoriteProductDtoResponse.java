package com.okbasalman.api_gateway.dto.auth;

import java.util.List;

import lombok.Data;

@Data
public class ToggleFavoriteProductDtoResponse {
    private String username;
    private List<String> toggleFavoriteProduc;
}
