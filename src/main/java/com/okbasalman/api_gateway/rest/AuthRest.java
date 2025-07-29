package com.okbasalman.api_gateway.rest;
// import java.util.List;

import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okbasalman.api_gateway.dto.auth.LoginDto;
import com.okbasalman.api_gateway.dto.auth.LogoutDto;
import com.okbasalman.api_gateway.dto.auth.RefreshDto;
import com.okbasalman.api_gateway.dto.auth.RegisterDto;
import com.okbasalman.api_gateway.dto.auth.ResponseLoginDto;
import com.okbasalman.api_gateway.dto.auth.UserInfoDto;
// import com.okbasalman.api_gateway.dto.product.DeleteProductResultDto;
// import com.okbasalman.api_gateway.dto.product.ProductDto;
import com.okbasalman.api_gateway.grpc.AuthGrpc;

import jakarta.ws.rs.HeaderParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.okbasalman.api_gateway.grpc.ProductGrpc;
// import com.okbasalman.api_gateway.dto.product.ProductCreateDto;
@RestController
@RequestMapping("/auth")
public class AuthRest {
    private final AuthGrpc authGrpc;

     public AuthRest(AuthGrpc authGrpc) {
        this.authGrpc = authGrpc;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto request){
        return authGrpc.register(request);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto request){
        return authGrpc.login(request);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto request){
        return authGrpc.logout(request);
    }
    // @PostMapping("/refreshToken")
    // public ResponseEntity<?> refreshToken(@RequestBody RefreshDto request){
    //     return authGrpc.refreshToken(request);
    // }
    // @GetMapping("/userInfo")
    // public ResponseEntity<?> userInfo(@RequestHeader("Authorization") String accessToken){
    //     return authGrpc.getUserInfo(accessToken);
    // }
    
}
