package com.okbasalman.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.okbasalman.api_gateway.domain.port.input.ProductApiUseCase;
import com.okbasalman.api_gateway.domain.port.output.ProductClientPort;
import com.okbasalman.api_gateway.domain.service.ProductApiService;

@Configuration
public class ProductServiceConfig {

    @Bean
    public ProductApiUseCase productApiUseCase(ProductClientPort productClientPort){
        return new ProductApiService(productClientPort);
    }
}
