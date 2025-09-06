package com.okbasalman.api_gateway.rest;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.okbasalman.api_gateway.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.dto.product.GetVariantByDetailsDto;
import com.okbasalman.api_gateway.dto.product.ProductDto;
import com.okbasalman.api_gateway.dto.product.ProductVariantDto;
import com.okbasalman.api_gateway.dto.product.ProductVariantIdDto;
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
        return  productGrpc.getProductById(id);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(){
        
        return productGrpc.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDto dto,@AuthenticationPrincipal Jwt jwt){

        try {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");


        Map<String, Object> serviceAccess =
                (Map<String, Object>) resourceAccess.get("authentication-service");

        List<String> roles = (List<String>) serviceAccess.get("roles");
        String role=(String)roles.get(0);
        if(role.equals("ADMIN"))
        return ResponseEntity.status(200).body(productGrpc.createProduct(dto));
        return ResponseEntity.status(401).body("Invalid credential");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error: "+e.getMessage());
        }
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

    @GetMapping("/variant/{id}")
    public ProductVariantDto getVariantById(@PathVariable Long id) {
    return productGrpc.getVariantById(id);

    
}

@PostMapping("/variant")
public ProductVariantIdDto getVariantByDetails(@RequestBody GetVariantByDetailsDto dto) {
    Long variantId = productGrpc.getVariantByDetails(dto.getProductId(), dto.getSize(), dto.getColor());
    return new ProductVariantIdDto(variantId);
}

}
