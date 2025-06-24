package com.okbasalman.api_gateway.adapter.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okbasalman.api_gateway.domain.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.domain.dto.product.ProductCreateDto;
import com.okbasalman.api_gateway.domain.model.Product;
import com.okbasalman.api_gateway.domain.port.input.ProductApiUseCase;

@RestController
@RequestMapping("/api/products")
public class ProductRestAdapter {
    private final ProductApiUseCase productApiUseCase;

    public ProductRestAdapter(ProductApiUseCase productApiUseCase){
        this.productApiUseCase = productApiUseCase;
    }

    @GetMapping("/{id}")
    public Product geProduct(@PathVariable Integer id){
        return  productApiUseCase.getProductById(id);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productApiUseCase.getAllProducts();
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductCreateDto dto){
        return productApiUseCase.createProduct(dto);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product){
        return productApiUseCase.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public DeleteProductResultDto deleteProduct(@PathVariable Integer id){
        return productApiUseCase.deleteProduct(id);
    }
}
