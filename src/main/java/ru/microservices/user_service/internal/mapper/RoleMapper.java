package ru.microservices.user_service.internal.mapper;

import org.springframework.stereotype.Component;
import ru.microservices.proto.RoleModel;
import ru.microservices.user_service.internal.entity.Role;

@Component
public class RoleMapper {

    public RoleModel toRoleModel(Role role) {
        return RoleModel.newBuilder()
                .setId(role.getId())
                .setKey(role.getKey())
                .build();
    }
}
