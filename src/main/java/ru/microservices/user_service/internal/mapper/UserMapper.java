package ru.microservices.user_service.internal.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.proto.UserModel;
import ru.microservices.proto.UserResponse;
import ru.microservices.proto.UsersResponse;
import ru.microservices.proto.ValidateResponse;
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

    public UserResponse toUserResponse(User user) {
        return UserResponse.newBuilder()
                .setUser(toUserModel(user))
                .build();
    }

    public UsersResponse toUsersResponse(List<User> users) {
        return UsersResponse.newBuilder()
                .addAllUsers(
                        users
                                .stream()
                                .map(this::toUserModel)
                                .toList()
                )
                .build();
    }

    public ValidateResponse toValidateResponse(Boolean value) {
        return ValidateResponse.newBuilder()
                .setValue(value)
                .build();
    }
}
