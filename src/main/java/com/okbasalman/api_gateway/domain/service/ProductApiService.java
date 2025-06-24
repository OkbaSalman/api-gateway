package com.okbasalman.api_gateway.domain.service;

import java.util.List;

import com.okbasalman.api_gateway.domain.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.domain.dto.product.ProductCreateDto;
import com.okbasalman.api_gateway.domain.model.Product;
import com.okbasalman.api_gateway.domain.port.input.ProductApiUseCase;
import com.okbasalman.api_gateway.domain.port.output.ProductClientPort;

public class ProductApiService implements ProductApiUseCase {
    private final ProductClientPort productClientPort;

    public ProductApiService(ProductClientPort productClientPort){
        this.productClientPort = productClientPort;
    }

    @Override
    public Product getProductById(Integer id) {
        return productClientPort.getProductById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productClientPort.getAllProducts();
    }

    @Override
    public Product createProduct(ProductCreateDto dto) {
        return productClientPort.createProduct(dto);
    }

    @Override
    public Product updateProduct(Product product) {
        return productClientPort.updateProduct(product);
    }

    @Override
    public DeleteProductResultDto deleteProduct(Integer id) {
        return productClientPort.deleteProduct(id);
    }
}
