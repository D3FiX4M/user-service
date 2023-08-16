package ru.microservices.user_service.core.exception;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import io.grpc.*;
import io.grpc.protobuf.StatusProto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.user_service.core.InstanceConfig;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ExtendedServerExceptionHandler implements ServerInterceptor {

    private final InstanceConfig instanceConfig;

    @Override
    public <T, R> ServerCall.Listener<T> interceptCall(
            ServerCall<T, R> serverCall, Metadata headers, ServerCallHandler<T, R> serverCallHandler) {
        ServerCall.Listener<T> delegate = serverCallHandler.startCall(serverCall, headers);
        return new ExceptionHandler<>(delegate, serverCall, instanceConfig);
    }

    private static class ExceptionHandler<T, R>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<T> {

        private final InstanceConfig instanceConfig;

        private final ServerCall<T, R> delegate;


        ExceptionHandler(
                ServerCall.Listener<T> listener, ServerCall<T, R> serverCall, InstanceConfig instanceConfig) {
            super(listener);
            this.delegate = serverCall;
            this.instanceConfig = instanceConfig;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, delegate);
                throw ex;
            }
        }


        private void handleException(
                RuntimeException exception,
                ServerCall<T, R> serverCall) {
            // Catch specific Exception and Process

            Status status = null;
            Metadata headers = null;

            if (exception instanceof StatusRuntimeException) {

                com.google.rpc.Status extractedStatus = StatusProto.fromThrowable(exception);

                if (extractedStatus != null) {

                    List<Any> detailsList = new ArrayList<>(extractedStatus.getDetailsList());


                    ExtendedError extendedError = ExtendedError.INTERNAL;

                    Map<String, String> metaData = fillMetaData(
                            extendedError,
                            exception,
                            serverCall
                    );


                    ErrorInfo errorInfo = fillNewErrorInfo(
                            metaData,
                            extendedError
                    );


                    Any newDetail = Any.pack(errorInfo);

                    detailsList.add(newDetail);

                    com.google.rpc.Status newStatus = extractedStatus.toBuilder()
                            .setCode(
                                    extendedError
                                            .getGrpcStatus()
                                            .getCode()
                                            .value()
                            )
                            .clearDetails()
                            .addAllDetails(detailsList)
                            .build();

                    var statusRuntimeException = StatusProto.toStatusRuntimeException(newStatus);
                    status = statusRuntimeException.getStatus();
                    headers = statusRuntimeException.getTrailers();
                }

            } else {

                ExtendedException extendedException;

                if (exception instanceof ExtendedException) {
                    extendedException = (ExtendedException) exception;
                } else {
                    extendedException = ExtendedException.of(exception);
                }

                ExtendedError extendedError = extendedException.getError();

                Map<String, String> metaData = fillMetaData(
                        extendedError,
                        extendedException,
                        serverCall
                );

                ErrorInfo errorInfo = fillNewErrorInfo(
                        metaData,
                        extendedError
                );


                com.google.rpc.Status newStatus = com.google.rpc.Status.newBuilder()
                        .setCode(
                                extendedError
                                        .getGrpcStatus()
                                        .getCode()
                                        .value()
                        )
                        .addDetails(Any.pack(errorInfo))
                        .build();


                var statusRuntimeException = StatusProto.toStatusRuntimeException(newStatus);

                status = Status.fromThrowable(statusRuntimeException);
                headers = statusRuntimeException.getTrailers();
            }


            serverCall.close(status, headers);
        }


        private ErrorInfo fillNewErrorInfo(
                Map<String, String> metadata,
                ExtendedError extendedError
        ) {

            return
                    ErrorInfo.newBuilder()
                            .setReason(
                                    extendedError
                                            .name()
                            )
                            .setDomain(
                                    Map.of(
                                            "ID", instanceConfig.getId(),
                                            "KEY", instanceConfig.getKey()
                                    ).toString()
                            )
                            .putAllMetadata(metadata)
                            .build();

        }

        private Map<String, String> fillMetaData(
                ExtendedError error,
                Exception exception,
                ServerCall<T, R> serverCall) {

            Map<String, String> map = new HashMap<>();

            map.put("grpcStatus", error.getGrpcStatus().getCode().name());
            map.put("httpStatus", error.getHttpStatus().name());

            map.put(
                    "description",
                    exception.getMessage() != null
                            && !exception.getMessage().isEmpty()
                            && !exception.getMessage().endsWith(": ")
                            ? exception.getMessage()
                            : "");

            map.put("methodName", serverCall.getMethodDescriptor().getFullMethodName());
            map.put("time", LocalDateTime.now().toString());
            map.put("stackTrace", Arrays.toString(exception.getStackTrace()));

            return map;
        }


    }
}
