package com.okbasalman.api_gateway.rest;

import com.okbasalman.api_gateway.dto.order.*;
import com.okbasalman.api_gateway.grpc.OrderGrpc;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderRest {

    private final OrderGrpc orderGrpc;

    public OrderRest(OrderGrpc orderGrpc) {
        this.orderGrpc = orderGrpc;
    }

    @PostMapping
    public CreateOrderResultDto createOrder(@RequestBody OrderCreateDto dto ,@AuthenticationPrincipal Jwt jwt) {
        System.out.println("order is here");
        Map<String, Object> claims = jwt.getClaims();
        String email =(String) claims.get("email");
        String id =(String) claims.get("sub");
        System.out.println(id+email+dto);
        return orderGrpc.createOrder(dto,email,id);
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

    @GetMapping("/{orderId}/status")
    public UpdateOrderStatusResultDto updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderGrpc.updateOrderStatus(orderId, status);
    }
}
