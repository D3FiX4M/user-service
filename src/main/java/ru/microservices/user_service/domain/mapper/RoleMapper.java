package ru.microservices.user_service.domain.mapper;

import org.springframework.stereotype.Component;
import ru.microservices.user_service.RoleModel;
import ru.microservices.user_service.domain.entity.Role;

@Component
public class RoleMapper {

    public RoleModel toRoleModel(Role role) {
        return RoleModel.newBuilder()
                .setId(role.getId())
                .setKey(role.getKey())
                .build();
    }
}
