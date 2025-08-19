package com.okbasalman.api_gateway.grpc;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.email.grpc.*;
import com.okbasalman.api_gateway.dto.email.CheckEmailDto;
import com.okbasalman.api_gateway.dto.email.SendEmailDto;

import org.springframework.stereotype.Component;

@Component
public class EmailGrpc {

    private final EmailServiceGrpc.EmailServiceBlockingStub emailStub;

    public EmailGrpc() {
        // 👇 Adjust host/port to where your Email service is running
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        emailStub = EmailServiceGrpc.newBlockingStub(channel);
    }

    public boolean sendEmail(SendEmailDto sendEmailDto) {
        SendEmailRequest request = SendEmailRequest.newBuilder()
                .setEmail(sendEmailDto.getEmail())
                .setUsername(sendEmailDto.getUsername())
                .build();

        SendEmailResponse response = emailStub.sendEmail(request);
        return response.getSuccess();
    }

    public boolean checkEmail(CheckEmailDto checkEmailDto) {
        VerifyCodeRequest request = VerifyCodeRequest.newBuilder()
                .setEmail(checkEmailDto.getEmail())
                .setCode(checkEmailDto.getCode())
                .build();
                VerifyCodeResponse response = emailStub.checkEmail(request);
        return response.getSuccess();
    }
}
