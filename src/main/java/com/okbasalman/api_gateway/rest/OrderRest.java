package com.okbasalman.api_gateway.rest;

import com.okbasalman.api_gateway.dto.order.*;
import com.okbasalman.api_gateway.grpc.OrderGrpc;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRest {

    private final OrderGrpc orderGrpc;

    public OrderRest(OrderGrpc orderGrpc) {
        this.orderGrpc = orderGrpc;
    }

    @PostMapping
    public CreateOrderResultDto createOrder(@RequestBody OrderCreateDto dto) {
        return orderGrpc.createOrder(dto);
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderGrpc.getAllOrders();
    }

    @GetMapping("/user/{userId}")
    public List<OrderDto> getUserOrders(@PathVariable String userId) {
        return orderGrpc.getMyOrders(userId);
    }

    @DeleteMapping("/{orderId}")
    public DeleteOrderResultDto deleteOrder(@PathVariable Long orderId) {
        return orderGrpc.deleteOrder(orderId);
    }

    @PutMapping("/{orderId}/status")
    public UpdateOrderStatusResultDto updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderGrpc.updateOrderStatus(orderId, status);
    }
}
