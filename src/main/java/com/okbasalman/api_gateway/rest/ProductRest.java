package com.okbasalman.api_gateway.rest;

import java.util.List;


import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ProductDto geProduct(@PathVariable Long id){
        System.out.println("1231243");
        return  productGrpc.getProductById(id);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(@AuthenticationPrincipal Jwt jwt){
        //  if (jwt == null) {
        //     return List.of();
        // }

        // Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        // if (resourceAccess == null) {
        //     return List.of();
        // }

        // Map<String, Object> serviceAccess =
        //         (Map<String, Object>) resourceAccess.get("authentication-service");
        // if (serviceAccess == null) {
        //     return List.of();
        // }

        // List<String> roles = (List<String>) serviceAccess.get("roles");
        return productGrpc.getAllProducts();
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductCreateDto dto){
        return productGrpc.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestBody ProductDto product){
        System.out.println(product);
        return productGrpc.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public DeleteProductResultDto deleteProduct(@PathVariable Integer id){
        return productGrpc.deleteProduct(id);
    }
}
