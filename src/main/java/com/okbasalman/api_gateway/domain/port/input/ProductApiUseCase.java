package com.okbasalman.api_gateway.domain.port.input;

import java.util.List;

import com.okbasalman.api_gateway.domain.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.domain.dto.product.ProductCreateDto;
import com.okbasalman.api_gateway.domain.model.Product;

public interface ProductApiUseCase {
    Product createProduct(ProductCreateDto dto);
    List<Product> getAllProducts();
    Product getProductById(Integer id);
    Product updateProduct(Product product);
    DeleteProductResultDto deleteProduct(Integer id);
}
