package com.okbasalman.api_gateway.adapter.output.grpc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.okbasalman.api_gateway.domain.dto.product.DeleteProductResultDto;
import com.okbasalman.api_gateway.domain.dto.product.ProductCreateDto;
import com.okbasalman.api_gateway.domain.model.Product;
import com.okbasalman.api_gateway.domain.port.output.ProductClientPort;
import com.okbasalman.grpc.CreateProductRequest;
import com.okbasalman.grpc.DeleteProductRequest;
import com.okbasalman.grpc.DeleteProductResponse;
import com.okbasalman.grpc.Empty;
import com.okbasalman.grpc.GetProductByIdRequest;
import com.okbasalman.grpc.ProductListResponse;
import com.okbasalman.grpc.ProductResponse;
import com.okbasalman.grpc.ProductServiceGrpc;
import com.okbasalman.grpc.UpdateProductRequest;

import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class ProductGrpcAdapter implements ProductClientPort{

    @GrpcClient("productService")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Override
    public List<Product> getAllProducts(){
        ProductListResponse response = serviceBlockingStub.getAllProducts(Empty.newBuilder().build());

        return response.getProductsList().stream().map(e -> new Product(e.getId(), e.getName(), e.getPrice(), e.getStock())).collect(Collectors.toList());
    }

    @Override
    public Product createProduct(ProductCreateDto dto) {
        ProductResponse response = serviceBlockingStub.createProduct(CreateProductRequest.newBuilder().setName(dto.getName()).setPrice(dto.getPrice()).setStock(dto.getStock()).build());

        return new Product(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    @Override
    public Product getProductById(Integer id) {
        ProductResponse response = serviceBlockingStub.getProductById(GetProductByIdRequest.newBuilder().setId(id).build());

        return new Product(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    @Override
    public Product updateProduct(Product product) {
        ProductResponse response = serviceBlockingStub.updateProduct(UpdateProductRequest.newBuilder().setId(product.getId()).setName(product.getName()).setPrice(product.getPrice()).setStock(product.getStock()).build());

        return new Product(response.getId(), response.getName(), response.getPrice(), response.getStock());
    }

    @Override
    public DeleteProductResultDto deleteProduct(Integer id) {
        DeleteProductResponse response = serviceBlockingStub.deleteProduct(DeleteProductRequest.newBuilder().setId(id).build());

        return new DeleteProductResultDto(response.getSuccess(), response.getMessage());
    }
}
