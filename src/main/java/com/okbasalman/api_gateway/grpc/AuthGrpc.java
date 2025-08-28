package com.okbasalman.api_gateway.grpc;

import com.okbasalman.grpcauth.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.okbasalman.api_gateway.dto.auth.*;
@Service
public class AuthGrpc {
    private ManagedChannel channel;
    private AuthServiceGrpc.AuthServiceBlockingStub stub;
    @Value("${api.gateway-key}")
    private String apiGatewayKey;
    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() 
                .build();
        stub = AuthServiceGrpc
            .newBlockingStub(channel);

        System.out.println("Auth gRPC channel initialized successfully");
    }

    public ResponseEntity<?> register(RegisterDto registerDto){
        System.out.println("regis1: "+registerDto);
        try {
            
            RegisterResponse response=stub.register(User.newBuilder().setEmail(registerDto.getEmail()).setPassword(registerDto.getPassword()).setUsername(registerDto.getUsername()).build());
            System.out.println(response.getMessage());
            return ResponseEntity.status(response.getStatusCode()).body(response.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("not found");
        }
    }
    public ResponseEntity<?> login(LoginDto loginDto){
        try {
            LoginResponse response=stub.login(LoginRequest.newBuilder().setEmail(loginDto.getEmail()).setPassword(loginDto.getPassword()).build());
            ResponseLoginDto res= new ResponseLoginDto(response.getAccessToken(),response.getExpiresIn(),response.getRefreshExpiresIn(),response.getRefreshToken(),response.getRole());
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            if(e.getLocalizedMessage().equals("DATA_LOSS: User not Verified"))
            return ResponseEntity.status(403).body("User not Verified");
            return ResponseEntity.status(404).body("not found");
        }
    }

    public ResponseEntity<?> logout(String logoutDto){
        try {
            
            LogoutResponse response=stub.logout(LogoutRequest.newBuilder().setRefreshToken(logoutDto).build());
            
            return ResponseEntity.status(200).body(response.getLogout());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Can not logout");
        }
    }

    public ResponseEntity<?> refreshToken(String refreshDto){
        try {
            
            RefreshResponse response=stub.refreshToken(RefreshRequest.newBuilder().setRefreshToken(refreshDto).build());
            ResponseLoginDto res= new ResponseLoginDto(response.getAccessToken(),response.getExpiresIn(),response.getRefreshExpiresIn(),response.getRefreshToken(),response.getRole());
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Refresh Token is expired");
        }
    }
    public ResponseEntity<?> saveEmail(String email){
        try {
            stub.saveEmail(VerifyEmailRequest.newBuilder().setEmail(email).build());
            
            return ResponseEntity.status(200).body("res");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Refresh Token is expired");
        }
    }
    public ResponseEntity<?> getUserInfo(String email ){
        try {
            UserInfoResponse response = stub.getUserInfo(UserInfoRequest.newBuilder().setEmail(email).build());
            ToggleFavoriteProductDtoResponse toggleFavoriteProductDtoResponse=new ToggleFavoriteProductDtoResponse();
            toggleFavoriteProductDtoResponse.setUsername(response.getUsername());
            toggleFavoriteProductDtoResponse.setToggleFavoriteProduc(response.getFavoriteProductsList());
            System.out.println("asdads:"+response.getFavoriteProductsList());
            return ResponseEntity.status(200).body(toggleFavoriteProductDtoResponse);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error in user info");
        }
    }
    public ResponseEntity<?> deleteUser(String email){
        try {
            stub.deleteUser(DeleteRequest.newBuilder().setEmail(email).build());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error delete user");
        }
    }
    public ResponseEntity<?> toggleFavoriteProduct(String email,Long id){
        try {
            System.out.println("email12334");
            ToggleFavoriteProductResponse res= stub.toggleFavoriteProduct(ToggleFavoriteProductRequest.newBuilder().setEmail(email).setId(id).build());
            return ResponseEntity.status(res.getSuccess()?200:400).body("");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error");
        }
    }
        public ResponseEntity<?> getAllUsers(){
        try {
            System.out.println("email12334");
            UsersResponse res= stub.getAllUsers(Empty.newBuilder().build());
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("error");
        }
    }
}
