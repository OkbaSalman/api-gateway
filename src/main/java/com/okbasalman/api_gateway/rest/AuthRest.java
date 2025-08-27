package com.okbasalman.api_gateway.rest;



import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okbasalman.api_gateway.dto.auth.DeleteDto;
import com.okbasalman.api_gateway.dto.auth.LoginDto;

import com.okbasalman.api_gateway.dto.auth.RegisterDto;
import com.okbasalman.api_gateway.dto.auth.ResponseLoginDto;
import com.okbasalman.api_gateway.dto.auth.ToggleFavoriteProductDto;
import com.okbasalman.api_gateway.grpc.AuthGrpc;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
@RequestMapping("/auth")
public class AuthRest {
    private final AuthGrpc authGrpc;

     public AuthRest(AuthGrpc authGrpc) {
        this.authGrpc = authGrpc;
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginDto request,HttpServletResponse response){
        System.out.println("null12131222");
        ResponseEntity<?> loginResponse = authGrpc.login(request);
        if (loginResponse.getStatusCode().is2xxSuccessful()) {
            ResponseLoginDto tokens = (ResponseLoginDto) loginResponse.getBody();
            if(!tokens.getRole().equals("ADMIN")){
        
            return ResponseEntity.notFound().build();
        }

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
                                  tokens.getExpires_in(), 0, null,tokens.getRole());
        return ResponseEntity.ok(bodyWithoutRefresh);
    }
    return loginResponse;
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
                                  tokens.getExpires_in(), 0, null,tokens.getRole());
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
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String request){
        return authGrpc.logout(request);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String request){
        ResponseEntity<?> refreshResponse= authGrpc.refreshToken(request);
        System.out.println("req: "+request);
        if(refreshResponse.getStatusCode().is2xxSuccessful()){
            ResponseLoginDto tokens = (ResponseLoginDto) refreshResponse.getBody();
            ResponseLoginDto bodyWithoutRefresh = new ResponseLoginDto(tokens.getAccess_token(),
                                  tokens.getExpires_in(), 0, null,tokens.getRole());
        return ResponseEntity.ok(bodyWithoutRefresh);
        }
        return refreshResponse;
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> userInfo(@AuthenticationPrincipal Jwt jwt){
        System.out.println("null123");
        Map<String, Object> claims = jwt.getClaims();
        String email =(String) claims.get("email");
        return authGrpc.getUserInfo(email);
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteDto deleteDto){
        
        return authGrpc.deleteUser(deleteDto.getEmail());
    }
    @PostMapping("/toggleFavoriteProduct")
    public ResponseEntity<?> toggleFavoriteProduct(@RequestBody ToggleFavoriteProductDto toggleFavoriteProductDto,@AuthenticationPrincipal Jwt jwt){
        System.out.println("12331241");
        Map<String, Object> claims = jwt.getClaims();
        String email =(String) claims.get("email");
        return authGrpc.toggleFavoriteProduct(email,toggleFavoriteProductDto.getId());
    }
    
}
