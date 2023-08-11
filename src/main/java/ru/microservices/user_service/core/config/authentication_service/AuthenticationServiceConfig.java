package ru.microservices.user_service.core.config.authentication_service;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationServiceConfig {
    @Value("${grpc.client.authentication-service.address}")
    private String address;
    @Value("${grpc.client.authentication-service.port}")
    private Integer port;

    public Channel getChannel() {
        return ManagedChannelBuilder.forAddress(
                this.address,
                this.port
        ).usePlaintext().build();
    }
}
