package com.okbasalman.api_gateway.grpc;

import com.okbasalman.grpcauth.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;

// import com.okbasalman.api_gateway.config.ApiKeyClientInterceptor;
import com.okbasalman.api_gateway.dto.auth.*;
@Service
public class AuthGrpc {
    private ManagedChannel channel;
    private AuthServiceGrpc.AuthServiceBlockingStub stub;
    @Value("${api.gateway-key}")
    private String apiGatewayKey;
    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("16.171.227.90", 9090)
                .usePlaintext() // Use plaintext for local development
                .build();
                // Create metadata with API key header
        // Metadata metadata = new Metadata();
        // Metadata.Key<String> apiKeyHeader = Metadata.Key.of("x-api-gateway-key", Metadata.ASCII_STRING_MARSHALLER);
        // metadata.put(apiKeyHeader, apiGatewayKey);

        // Attach metadata to stub so all calls include the header
        stub = AuthServiceGrpc
            .newBlockingStub(channel);
            // .withInterceptors(new ApiKeyClientInterceptor(apiGatewayKey));
        // stub = MetadataUtils.attachHeaders(stub, metadata);
        // stub = AuthServiceGrpc.newBlockingStub(channel);
        System.out.println("gRPC channel initialized successfully");
    }

    public ResponseEntity<?> register(RegisterDto registerDto){
        try {
            
            RegisterResponse response=stub.register(User.newBuilder().setEmail(registerDto.getEmail()).setPassword(registerDto.getPassword()).setUsername(registerDto.getUsername()).build());
            return ResponseEntity.status(200).body(response.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(404).body("not found");
        }
    }
    public ResponseEntity<?> login(LoginDto loginDto){
        try {
            LoginResponse response=stub.login(LoginRequest.newBuilder().setEmail(loginDto.getEmail()).setPassword(loginDto.getPassword()).build());
            ResponseLoginDto res= new ResponseLoginDto(response.getAccessToken(),response.getExpiresIn(),response.getRefreshExpiresIn(),response.getRefreshToken());
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("not found");
        }
    }

    public ResponseEntity<?> logout(LogoutDto logoutDto){
        try {
            
            LogoutResponse response=stub.logout(LogoutRequest.newBuilder().setRefreshToken(logoutDto.getRefreshToken()).build());
            
            return ResponseEntity.status(200).body(response.getLogout());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Can not logout");
        }
    }

    public ResponseEntity<?> refreshToken(String refreshDto){
        try {
            
            RefreshResponse response=stub.refreshToken(RefreshRequest.newBuilder().setRefreshToken(refreshDto).build());
            ResponseLoginDto res= new ResponseLoginDto(response.getAccessToken(),response.getExpiresIn(),response.getRefreshExpiresIn(),response.getRefreshToken());
            return ResponseEntity.status(200).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Refresh Token is expired");
        }
    }
    // public ResponseEntity<?> getUserInfo(String accessToken){
    //     try {
    //         Metadata metadata = new Metadata();
    //     Metadata.Key<String> AUTHORIZATION_HEADER =
    //         Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    //     metadata.put(AUTHORIZATION_HEADER, "Bearer " + accessToken);

    //     // Correct way to attach headers
    //     AuthServiceGrpc.AuthServiceBlockingStub stubWithHeader = 
    //         stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));

    //     UserInfoResponse response = stubWithHeader.getUserInfo(Empty.newBuilder().build());
    //     System.out.println("response: " + response);
    //         UserInfoDto res= new UserInfoDto(response.getId(),response.getUsername(),response.getEmail());
    //         return ResponseEntity.status(200).body(res);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(400).body("error in user info");
    //     }
    // }
}
