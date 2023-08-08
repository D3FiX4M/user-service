package ru.microservices.user_service.external;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.microservices.proto.UserByIdsRequest;
import ru.microservices.proto.UserListResponse;
import ru.microservices.user_service.internal.mapper.UserMapper;
import ru.microservices.user_service.internal.service.UserService;
import ru.microservices.user_service.util.StreamObserverUtils;

@GRpcService
@RequiredArgsConstructor
public class UserServiceGrpc extends ru.microservices.proto.UserServiceGrpc.UserServiceImplBase {

    private final UserService service;
    private final UserMapper mapper;

    @Override
    public void getUserById(UserByIdsRequest request, StreamObserver<UserListResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserListResponse(
                        service.get(
                                request.getIdList()
                        )
                )
        );
    }

    @Override
    public void getAllUser(Empty request, StreamObserver<UserListResponse> responseObserver) {
        StreamObserverUtils.actionValue(
                responseObserver,
                () -> mapper.toUserListResponse(
                        service.getAll()
                )
        );
    }
}
