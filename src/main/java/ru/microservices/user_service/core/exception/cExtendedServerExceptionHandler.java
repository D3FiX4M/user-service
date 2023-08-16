package ru.microservices.user_service.core.exception;

import com.google.protobuf.Any;
import com.google.rpc.ErrorInfo;
import io.grpc.*;
import io.grpc.protobuf.StatusProto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.microservices.user_service.core.InstanceConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class cExtendedServerExceptionHandler implements ServerInterceptor {

    private final InstanceConfig instanceConfig;

    @Override
    public <T, R> ServerCall.Listener<T> interceptCall(
            ServerCall<T, R> serverCall, Metadata headers, ServerCallHandler<T, R> serverCallHandler) {
        ServerCall.Listener<T> delegate = serverCallHandler.startCall(serverCall, headers);
        return new ExceptionHandler<>(delegate, serverCall, headers, instanceConfig);
    }

    private static class ExceptionHandler<T, R>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<T> {

        private final InstanceConfig instanceConfig;

        private final ServerCall<T, R> delegate;
        private final Metadata headers;

        ExceptionHandler(
                ServerCall.Listener<T> listener, ServerCall<T, R> serverCall, Metadata headers, InstanceConfig instanceConfig) {
            super(listener);
            this.delegate = serverCall;
            this.headers = headers;
            this.instanceConfig = instanceConfig;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (RuntimeException ex) {
                handleException(ex, delegate, headers);
                throw ex;
            }
        }

        private void handleException(
                RuntimeException exception,
                ServerCall<T, R> serverCall,
                Metadata headers) {
            // Catch specific Exception and Process

            ExtendedException extendedException;

            if (exception instanceof ExtendedException) {
                extendedException = (ExtendedException) exception;
            } else {
                extendedException = ExtendedException.of(exception);
            }

            Map<String, String> metaData = fillMetaData(extendedException, serverCall);
            // Build google.rpc.ErrorInfo
            var errorInfo =
                    ErrorInfo.newBuilder()
                            .setReason(
                                    extendedException
                                            .getError()
                                            .name()
                            )
                            .setDomain(
                                    Map.of(
                                            "ID", instanceConfig.getId(),
                                            "KEY", instanceConfig.getKey()
                                    ).toString()
                            )
                            .putAllMetadata(metaData)
                            .build();

            com.google.rpc.Status rpcStatus =
                    com.google.rpc.Status.newBuilder()
                            .setCode(
                                    extendedException
                                            .getError()
                                            .getGrpcStatus()
                                            .getCode()
                                            .value()
                            )
                            .addDetails(Any.pack(errorInfo))
                            .build();

            var statusRuntimeException = StatusProto.toStatusRuntimeException(rpcStatus);

            var newStatus = Status.fromThrowable(statusRuntimeException);
            // Get metadata from statusRuntimeException
            Metadata newHeaders = statusRuntimeException.getTrailers();

            serverCall.close(newStatus, newHeaders);
        }

        private Map<String, String> fillMetaData(
                ExtendedException exception,
                ServerCall<T, R> serverCall) {

            ExtendedError error = exception.getError();


            Map<String, String> map = new HashMap<>();

            map.put("grpcStatus", error.getGrpcStatus().getCode().name());
            map.put("httpStatus", error.getHttpStatus().name());
            map.put("description", exception.getMessage() != null ? exception.getMessage() : "empty");
            map.put("methodName", serverCall.getMethodDescriptor().getFullMethodName());
            map.put("time", LocalDateTime.now().toString());
            map.put("stackTrace", Arrays.toString(exception.getStackTrace()));

            return map;
        }

    }
}
