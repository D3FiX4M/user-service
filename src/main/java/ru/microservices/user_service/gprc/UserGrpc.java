package ru.microservices.user_service.gprc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.user_service.*;
import ru.microservices.user_service.domain.mapper.UserMapper;
import ru.microservices.user_service.domain.service.UserService;
import ru.microservices.user_service.util.StreamObserverUtils;

@Component
@RequiredArgsConstructor
public class UserGrpc extends UserServiceGrpc.UserServiceImplBase {

    private final UserService service;
    private final UserMapper mapper;

    @Override
    public void create(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserResponse(
                        service.create(
                                request.getEmail(),
                                request.getPassword()
                        )
                )
        );
    }

    @Override
    public void getUser(UserIdRequest request, StreamObserver<UserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserResponse(
                        service.getUser(
                                request.getId()
                        )
                )
        );
    }

    @Override
    public void getUsers(UserMultipleIdRequest request, StreamObserver<UserMultipleResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserMultipleResponse(
                        service.getUsers(
                                request.getIdList()
                        )
                )
        );
    }
}
