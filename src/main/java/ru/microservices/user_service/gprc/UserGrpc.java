package ru.microservices.user_service.gprc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.microservices.user_service.*;
import ru.microservices.user_service.domain.mapper.UserMapper;
import ru.microservices.user_service.domain.service.UserService;
import ru.microservices.user_service.util.StreamObserverUtils;

@GRpcService
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
                                request.getUsername(),
                                request.getPassword()
                        )
                )
        );
    }

    @Override
    public void getById(UserIdRequest request, StreamObserver<UserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserResponse(
                        service.getById(
                                request.getId()
                        )
                )
        );
    }

    @Override
    public void getByIds(UserIdMultipleRequest request, StreamObserver<UserMultipleResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserMultipleResponse(
                        service.getByIds(
                                request.getIdList()
                        )
                )
        );
    }

    @Override
    public void getAllUser(Empty request, StreamObserver<UserMultipleResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserMultipleResponse(
                        service.getAll()
                )
        );
    }

    @Override
    public void getUserByUsername(UserUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserResponse(
                        service.loadUserByUsername(
                                request.getUsername()
                        )
                )
        );
    }

    @Override
    public void validateUser(ValidateUserRequest request, StreamObserver<ValidateUserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toValidateUserResponse(
                        service.validateUser(
                                request.getUsername(),
                                request.getPassword()
                        )
                )
        );
    }
}
