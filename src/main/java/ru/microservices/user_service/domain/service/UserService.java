package ru.microservices.user_service.domain.service;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.microservices.user_service.core.exception.ExtendedError;
import ru.microservices.user_service.core.exception.ExtendedException;
import ru.microservices.user_service.core.external.role_service.RoleService;
import ru.microservices.user_service.domain.entity.User;
import ru.microservices.user_service.domain.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User create(String email, String password) {

        if (repository.existsByEmail(email)) {
            throw ExtendedException.of(ExtendedError.ALREADY_EXIST);
        }

        Long roleId = roleService.defaultBlockingStub()
                .getRoleByDefault(
                        Empty.newBuilder()
                                .build())
                .getRole().getId();

        return repository.save(
                new User(
                        null,
                        email,
                        passwordEncoder.encode(password),
                        roleId
                )
        );
    }

    public User getUser(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> ExtendedException.of(ExtendedError.NOT_FOUND)
                );
    }

    public List<User> getUsers(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

}
