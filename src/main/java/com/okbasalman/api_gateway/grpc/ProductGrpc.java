package com.okbasalman.api_gateway.grpc;

import com.okbasalman.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.okbasalman.api_gateway.dto.product.*;

@Service
public class ProductGrpc {

    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        serviceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
        System.out.println("Product gRPC channel initialized successfully");
    }

    public List<ProductDto> getAllProducts() {
        ProductListResponse response = serviceBlockingStub.getAllProducts(Empty.newBuilder().build());
        return response.getProductsList().stream()
                .map(e -> new ProductDto(e.getId(), e.getName(), e.getPrice(), e.getStock()))
                .collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductCreateDto dto) {
        ProductResponse response = serviceBlockingStub.createProduct(
                CreateProductRequest.newBuilder()
                        .setName(dto.getName())
                        .setPrice(dto.getPrice())
                        .setStock(dto.getStock())
                        .build()
        );
        return new ProductDto(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    public ProductDto getProductById(Integer id) {
        ProductResponse response = serviceBlockingStub.getProductById(
                GetProductByIdRequest.newBuilder().setId(id).build()
        );
        return new ProductDto(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    public ProductDto updateProduct(ProductDto product) {
        ProductResponse response = serviceBlockingStub.updateProduct(
                UpdateProductRequest.newBuilder()
                        .setId(product.getId())
                        .setName(product.getName())
                        .setPrice(product.getPrice())
                        .setStock(product.getStock())
                        .build()
        );
        return new ProductDto(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    public DeleteProductResultDto deleteProduct(Integer id) {
        DeleteProductResponse response = serviceBlockingStub.deleteProduct(
                DeleteProductRequest.newBuilder().setId(id).build()
        );
        return new DeleteProductResultDto(response.getSuccess(), response.getMessage());
    }
}
