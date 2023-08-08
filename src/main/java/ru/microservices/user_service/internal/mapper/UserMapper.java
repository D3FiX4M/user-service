package ru.microservices.user_service.internal.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.proto.UserListResponse;
import ru.microservices.proto.UserModel;
import ru.microservices.user_service.internal.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;

    private UserModel toUserModel(User user) {
        return UserModel.newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .addAllRoles(
                        user.getRoles()
                                .stream()
                                .map(roleMapper::toRoleModel)
                                .toList()
                )
                .build();
    }

    public UserListResponse toUserListResponse(List<User> users) {
        return UserListResponse.newBuilder()
                .addAllUsers(users
                        .stream()
                        .map(this::toUserModel)
                        .toList())
                .build();
    }
}
