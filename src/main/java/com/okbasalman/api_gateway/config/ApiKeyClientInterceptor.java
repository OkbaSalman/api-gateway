package com.okbasalman.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;

import io.grpc.*;

public class ApiKeyClientInterceptor implements ClientInterceptor {

    private final String apiKey;
    private static final Metadata.Key<String> API_KEY_HEADER = Metadata.Key.of("X-API-GATEWAY-KEY", Metadata.ASCII_STRING_MARSHALLER);

    public ApiKeyClientInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(API_KEY_HEADER, apiKey);
                super.start(responseListener, headers);
            }
        };
    }
}
