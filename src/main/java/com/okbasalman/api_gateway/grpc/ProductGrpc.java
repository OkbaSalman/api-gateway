package com.okbasalman.api_gateway.grpc;

import com.okbasalman.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.google.protobuf.Empty;
import com.okbasalman.api_gateway.dto.product.*;

@Service
public class ProductGrpc {

    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("5.230.47.162", 9094)
                .usePlaintext()
                .build();

        serviceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);
        System.out.println("Product gRPC channel initialized successfully");
    }

    public List<ProductDto> getAllProducts() {
        ProductListResponse response = serviceBlockingStub.getAllProducts(Empty.newBuilder().build());
        return response.getProductsList().stream()
                .map(this::mapProductResponseToDto)
                .collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductCreateDto dto) {
        List<ProductVariantCreateRequest> variantRequests = dto.getVariants().stream()
                .map(variantDto -> ProductVariantCreateRequest.newBuilder()
                        .setPrice(variantDto.getPrice())
                        .setStock(variantDto.getStock())
                        .setColor(variantDto.getColor())
                        .setSize(variantDto.getSize())
                        .addAllBase64Images(variantDto.getBase64Images())
                        .build())
                .collect(Collectors.toList());

        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setSeason(dto.getSeason())
                .addAllVariants(variantRequests)
                .build();

        ProductResponse response = serviceBlockingStub.createProduct(request);
        return mapProductResponseToDto(response);
    }

    public ProductDto getProductById(long id) {
        ProductResponse response = serviceBlockingStub.getProductById(
                GetProductByIdRequest.newBuilder().setId(id).build()
        );
        return mapProductResponseToDto(response);
    }

    public ProductDto updateProduct(ProductDto product) {
        List<ProductVariantUpdateRequest> variantRequests = product.getVariants().stream()
                .map(variantDto -> {
                    ProductVariantUpdateRequest.Builder builder = ProductVariantUpdateRequest.newBuilder()
                            .setId(variantDto.getId())
                            .setPrice(variantDto.getPrice())
                            .setStock(variantDto.getStock())
                            .setColor(variantDto.getColor())
                            .setSize(variantDto.getSize());

                    List<ProductImageUpdateRequest> imageUpdateRequests = variantDto.getImages().stream()
                            .map(imageDto -> ProductImageUpdateRequest.newBuilder()
                                    .setId(imageDto.getId())
                                    .setBase64Data(imageDto.getBase64Data())
                                    .build())
                            .collect(Collectors.toList());

                    return builder.addAllImages(imageUpdateRequests).build();
                })
                .collect(Collectors.toList());

        UpdateProductRequest request = UpdateProductRequest.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setSeason(product.getSeason())
                .addAllVariants(variantRequests)
                .build();

        ProductResponse response = serviceBlockingStub.updateProduct(request);
        return mapProductResponseToDto(response);
    }

    public DeleteProductResultDto deleteProduct(long id) {
        DeleteProductResponse response = serviceBlockingStub.deleteProduct(
                DeleteProductRequest.newBuilder().setId(id).build()
        );
        return new DeleteProductResultDto(response.getSuccess(), response.getMessage());
    }

    private ProductDto mapProductResponseToDto(ProductResponse response) {
        List<ProductVariantDto> variants = response.getVariantsList().stream()
                .map(variantResponse -> {
                    List<ProductImageDto> images = variantResponse.getImagesList().stream()
                            .map(imageResponse -> new ProductImageDto(imageResponse.getId(), imageResponse.getBase64Data()))
                            .collect(Collectors.toList());

                    return new ProductVariantDto(
                            variantResponse.getId(),
                            variantResponse.getPrice(),
                            variantResponse.getStock(),
                            variantResponse.getColor(),
                            variantResponse.getSize(),
                            images
                    );
                })
                .collect(Collectors.toList());

        return new ProductDto(
                response.getId(),
                response.getName(),
                response.getDescription(),
                response.getSeason(),
                variants
        );
    }
}
