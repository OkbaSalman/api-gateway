package com.okbasalman.api_gateway.rest;

import com.okbasalman.api_gateway.dto.order.*;
import com.okbasalman.api_gateway.grpc.OrderGrpc;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/createOrderByAdmin")
    public ResponseEntity<?> createOrderByAdmin(@RequestBody CreateOrderByAdminDto dto ,@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");


        Map<String, Object> serviceAccess =
                (Map<String, Object>) resourceAccess.get("authentication-service");

        List<String> roles = (List<String>) serviceAccess.get("roles");
        String role=(String)roles.get(0);
        System.out.println(role);
        OrderCreateDto order=new OrderCreateDto();
        order.setItems(dto.getItems());
        order.setAddress(dto.getAddress());
        
        if(role.equals("ADMIN"))
        return ResponseEntity.status(200).body(orderGrpc.createOrder(order,dto.getEmail(),dto.getUser_id()));
        return ResponseEntity.status(401).body("Invalid credential");
        
    }
    @PostMapping
    public CreateOrderResultDto createOrder(@RequestBody OrderCreateDto dto ,@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        String email =(String) claims.get("email");
        String id =(String) claims.get("sub");
        System.out.println(id+email+dto);
        return orderGrpc.createOrder(dto,email,id);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");


        Map<String, Object> serviceAccess =
                (Map<String, Object>) resourceAccess.get("authentication-service");

        List<String> roles = (List<String>) serviceAccess.get("roles");
        String role=(String)roles.get(0);        
        if(role.equals("ADMIN"))
        return ResponseEntity.status(200).body(orderGrpc.getAllOrders());
        return ResponseEntity.status(401).body("Invalid credential");
    }

    @GetMapping("/getUserOrders")
    public List<OrderDto> getUserOrders(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> claims = jwt.getClaims();
        String userId =(String) claims.get("sub");
        return orderGrpc.getMyOrders(userId);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId,@AuthenticationPrincipal Jwt jwt) {
          Map<String, Object> resourceAccess = jwt.getClaim("resource_access");


        Map<String, Object> serviceAccess =
                (Map<String, Object>) resourceAccess.get("authentication-service");

        List<String> roles = (List<String>) serviceAccess.get("roles");
        String role=(String)roles.get(0);
        System.out.println(role);
        if(role.equals("ADMIN"))
        return ResponseEntity.status(200).body(orderGrpc.deleteOrder(orderId));
        else return ResponseEntity.status(401).body("Invalid credential");
    }

    @GetMapping("/{orderId}/status")
    public UpdateOrderStatusResultDto updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderGrpc.updateOrderStatus(orderId, status);
    }
}
