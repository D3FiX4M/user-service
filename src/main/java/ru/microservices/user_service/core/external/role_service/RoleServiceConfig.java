package ru.microservices.user_service.core.external.role_service;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "grpc.client.role-service")
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
