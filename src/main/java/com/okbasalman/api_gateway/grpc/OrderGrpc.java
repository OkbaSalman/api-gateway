package com.okbasalman.api_gateway.grpc;

import com.example.orderservice.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.okbasalman.api_gateway.dto.order.*;
import com.okbasalman.api_gateway.dto.order.OrderDto;

@Service
public class OrderGrpc {

    private ManagedChannel channel;
    private OrderServiceGrpc.OrderServiceBlockingStub orderStub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("5.230.47.162", 9092)
                .usePlaintext()
                .build();

        orderStub = OrderServiceGrpc.newBlockingStub(channel);
        System.out.println("Order gRPC channel initialized successfully");
    }

    public CreateOrderResultDto createOrder(OrderCreateDto dto,String email,String id) {
        CreateOrderRequest.Builder builder = CreateOrderRequest.newBuilder()
        .setUserId(id)
        .setEmail(email)
        .setAddress(dto.getAddress());
        dto.getItems().forEach(item ->
        System.out.println("item!"+item.getProduct_id()));
        dto.getItems().forEach(item ->
        builder.addItems(OrderItem.newBuilder()
        .setProductId(item.getProduct_id())
        .setQuantity(item.getQuantity())
        .setProductVariantId(item.getProduct_variant_id())
        .build())
        );
        System.out.println("sasa: "+dto.getAddress()+email+id);
        System.out.println("id2121");
        CreateOrderResponse response = orderStub.createOrder(builder.build());
        return new CreateOrderResultDto(response.getSuccess(), response.getMessage(), response.getOrderId());
    }

    public List<OrderDto> getAllOrders() {
        GetAllOrdersResponse response = orderStub.getAllOrders(Empty.newBuilder().build());

        return response.getOrdersList().stream().map(order ->
            new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                order.getStatus(),
                order.getItemsList().stream().map(item ->
                    new OrderItemDto(item.getProductId(), item.getProductVariantId(),item.getQuantity())
                ).collect(Collectors.toList()),
                order.getCreatedAt().getSeconds() * 1000 // or format timestamp if needed
            )
        ).collect(Collectors.toList());
    }

    public List<OrderDto> getMyOrders(String userId) {
        GetMyOrdersResponse response = orderStub.getMyOrders(
            GetMyOrdersRequest.newBuilder().setUserId(userId).build()
        );

        return response.getOrdersList().stream().map(order ->
            new OrderDto(
                order.getOrderId(),
                order.getUserId(),
                order.getStatus(),
                order.getItemsList().stream().map(item ->
                    new OrderItemDto(item.getProductId(), item.getProductVariantId(),item.getQuantity())
                ).collect(Collectors.toList()),
                order.getCreatedAt().getSeconds() * 1000
            )
        ).collect(Collectors.toList());
    }

    public DeleteOrderResultDto deleteOrder(Long orderId) {
        DeleteOrderResponse response = orderStub.deleteOrder(
            DeleteOrderRequest.newBuilder().setOrderId(orderId).build()
        );
        return new DeleteOrderResultDto(response.getSuccess(), response.getMessage());
    }

    public UpdateOrderStatusResultDto updateOrderStatus(Long orderId, String status) {
        UpdateOrderStatusResponse response = orderStub.updateOrderStatus(
            UpdateOrderStatusRequest.newBuilder()
                .setOrderId(orderId)
                .setStatus(status)
                .build()
        );
        return new UpdateOrderStatusResultDto(response.getSuccess(), response.getMessage());
    }
}
