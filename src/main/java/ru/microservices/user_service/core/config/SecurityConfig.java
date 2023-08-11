package ru.microservices.user_service.core.config;

import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.security.GrpcSecurity;
import org.lognet.springboot.grpc.security.GrpcSecurityConfigurerAdapter;
import org.lognet.springboot.grpc.security.jwt.JwtAuthProviderFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import ru.microservices.user_service.UserServiceGrpc;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends GrpcSecurityConfigurerAdapter {

    private final JwtDecoder jwtDecoder;


    @Override
    public void configure(GrpcSecurity builder) throws Exception {
        builder
                .authorizeRequests()
                .anyMethodExcluding(
                        UserServiceGrpc.getCreateMethod(),
                        UserServiceGrpc.getGetUserByUsernameMethod(),
                        UserServiceGrpc.getValidateUserMethod()
                )
                .authenticated()
                .and()
                .authenticationProvider(
                        JwtAuthProviderFactory.forAuthorities(
                                jwtDecoder
                        )
                );
    }
}
