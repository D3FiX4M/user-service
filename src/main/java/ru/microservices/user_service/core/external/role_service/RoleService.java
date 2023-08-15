package ru.microservices.user_service.core.external.role_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.role_service.RoleServiceGrpc;

@Component
@RequiredArgsConstructor
public class RoleService {
    private final RoleServiceConfig config;

    public RoleServiceGrpc.RoleServiceBlockingStub defaultBlockingStub() {
        return RoleServiceGrpc.newBlockingStub(
                config.getChannel()
        );
    }
}
