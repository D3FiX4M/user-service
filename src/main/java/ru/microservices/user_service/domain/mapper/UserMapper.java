package ru.microservices.user_service.domain.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.user_service.UserModel;
import ru.microservices.user_service.UserMultipleResponse;
import ru.microservices.user_service.UserResponse;
import ru.microservices.user_service.domain.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private UserModel toUserModel(User user) {
        return UserModel.newBuilder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setRoleId(user.getRoleId())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.newBuilder()
                .setUser(toUserModel(user))
                .build();
    }

    public UserMultipleResponse toUserMultipleResponse(List<User> users) {
        return UserMultipleResponse.newBuilder()
                .addAllUsers(
                        users
                                .stream()
                                .map(this::toUserModel)
                                .toList()
                )
                .build();
    }

}
