package com.okbasalman.api_gateway.rest;
// import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;


// import com.okbasalman.api_gateway.grpc.ProductGrpc;
// import com.okbasalman.api_gateway.dto.product.ProductCreateDto;
@RestController
@RequestMapping("/auth")
public class AuthRest {
    private final AuthGrpc authGrpc;

     public AuthRest(AuthGrpc authGrpc) {
        this.authGrpc = authGrpc;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto request,HttpServletResponse response){
         ResponseEntity<?> loginResponse = authGrpc.login(request);
    if (loginResponse.getStatusCode().is2xxSuccessful()) {
        ResponseLoginDto tokens = (ResponseLoginDto) loginResponse.getBody();

        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefresh_token());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(tokens.getRefresh_expires_in());
        response.addCookie(refreshTokenCookie);

        // Add SameSite manually
        String cookieValue = String.format("refreshToken=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=Strict",
            tokens.getRefresh_token(), tokens.getRefresh_expires_in());
        response.setHeader("Set-Cookie", cookieValue);

        ResponseLoginDto bodyWithoutRefresh = new ResponseLoginDto(tokens.getAccess_token(),
                                  tokens.getExpires_in(), 0, null);
        return ResponseEntity.ok(bodyWithoutRefresh);
    }
    return loginResponse;
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto request){
        System.out.println("null:aas");
        return authGrpc.register(request);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutDto request){
        return authGrpc.logout(request);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String request){
        ResponseEntity<?> refreshResponse= authGrpc.refreshToken(request);
        if(refreshResponse.getStatusCode().is2xxSuccessful()){
            ResponseLoginDto tokens = (ResponseLoginDto) refreshResponse.getBody();
            ResponseLoginDto bodyWithoutRefresh = new ResponseLoginDto(tokens.getAccess_token(),
                                  tokens.getExpires_in(), 0, null);
        return ResponseEntity.ok(bodyWithoutRefresh);
        }
        return refreshResponse;
    }
    // @GetMapping("/userInfo")
    // public ResponseEntity<?> userInfo(@RequestHeader("Authorization") String accessToken){
    //     return authGrpc.getUserInfo(accessToken);
    // }
    
}
