package ru.microservices.user_service.core.exception;

import io.grpc.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GrpcError {

    // COMMON
    INTERNAL(Status.INTERNAL),
    NOT_FOUND(Status.NOT_FOUND),
    BAD_REQUEST(Status.INVALID_ARGUMENT),
    PARSER_ERROR(Status.INVALID_ARGUMENT),
    ALREADY_EXIST(Status.ALREADY_EXISTS);

    private final Status grpcStatus;
}
