package ru.microservices.user_service.core.config.internal;

import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.security.GrpcSecurity;
import org.lognet.springboot.grpc.security.GrpcSecurityConfigurerAdapter;
import org.lognet.springboot.grpc.security.jwt.JwtAuthProviderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends GrpcSecurityConfigurerAdapter {


    private static final String jwtSecret = "702ECF979164043FF68BD276F95409E6DC10167123D967F0AB78FA8DBCA02062";


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        var key = jwtSecret.getBytes();
        var originalKey = new SecretKeySpec(key, 0, key.length, "AES");
        return NimbusJwtDecoder.withSecretKey(originalKey).build();
    }

    @Override
    public void configure(GrpcSecurity builder) throws Exception {
        builder
                .authorizeRequests()
                .anyMethod()
                .authenticated()
                .and()
                .authenticationProvider(JwtAuthProviderFactory.forAuthorities(jwtDecoder()));
    }
}
