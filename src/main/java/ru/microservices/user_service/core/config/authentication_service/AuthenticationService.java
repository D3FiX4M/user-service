package ru.microservices.user_service.core.config.authentication_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.authentication_service.AuthenticationServiceGrpc;
import ru.microservices.user_service.UserServiceGrpc;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationServiceConfig config;

    public AuthenticationServiceGrpc.AuthenticationServiceBlockingStub authenticationServiceBlockingStub() {
        return AuthenticationServiceGrpc.newBlockingStub(
                config.getChannel()
        );
    }
}
