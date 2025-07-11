package com.okbasalman.api_gateway.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.okbasalman.api_gateway.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.dto.product.ProductDto;
import com.okbasalman.api_gateway.grpc.ProductGrpc;
import com.okbasalman.api_gateway.dto.product.ProductCreateDto;

@RestController
@RequestMapping("/api/products")
public class ProductRest {
    private final ProductGrpc productGrpc;

     public ProductRest(ProductGrpc productGrpc) {
        this.productGrpc = productGrpc;
    }

    @GetMapping("/{id}")
    public ProductDto geProduct(@PathVariable Integer id){
        return  productGrpc.getProductById(id);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(){
        return productGrpc.getAllProducts();
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductCreateDto dto){
        return productGrpc.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestBody ProductDto product){
        return productGrpc.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public DeleteProductResultDto deleteProduct(@PathVariable Integer id){
        return productGrpc.deleteProduct(id);
    }
}
