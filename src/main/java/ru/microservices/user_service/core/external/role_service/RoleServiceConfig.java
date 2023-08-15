package ru.microservices.user_service.core.external.role_service;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("grpc.client.role-service")
public class RoleServiceConfig {
    private String address;
    private Integer port;

    public Channel getChannel() {
        return ManagedChannelBuilder.forAddress(
                this.address,
                this.port
        ).usePlaintext().build();
    }
}