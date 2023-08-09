package ru.microservices.user_service.external;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.microservices.proto.*;
import ru.microservices.user_service.internal.mapper.UserMapper;
import ru.microservices.user_service.internal.service.UserService;
import ru.microservices.user_service.util.StreamObserverUtils;

@GRpcService
@RequiredArgsConstructor
public class UserServiceGrpc extends ru.microservices.proto.UserServiceGrpc.UserServiceImplBase {

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
    public void getById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
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
    public void getByIds(GetUserByIdsRequest request, StreamObserver<UsersResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUsersResponse(
                        service.getByIds(
                                request.getIdList()
                        )
                )
        );
    }

    @Override
    public void getAllUser(Empty request, StreamObserver<UsersResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUsersResponse(
                        service.getAll()
                )
        );
    }

    @Override
    public void existUserByUsername(UserByUsernameRequest request, StreamObserver<ExistResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toExistResponse(
                        service.existByUsername(
                                request.getUsername()
                        )
                )
        );
    }

    @Override
    public void getUserByUsername(UserByUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserResponse(
                        service.loadUserByUsername(
                                request.getUsername()
                        )
                )
        );
    }
}
