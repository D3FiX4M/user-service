package ru.microservices.user_service.internal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.microservices.proto.ERoleKey;
import ru.microservices.user_service.core.exception.ExtendedError;
import ru.microservices.user_service.core.exception.ExtendedException;
import ru.microservices.user_service.internal.entity.Role;
import ru.microservices.user_service.internal.entity.User;
import ru.microservices.user_service.internal.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public User create(String username, String password) {
        if (repository.existsByUsername(username)) {
            throw ExtendedException.of(ExtendedError.ALREADY_EXIST);
        }
        List<Role> roles = List.of(
                roleService.getByKey(ERoleKey.USER)
        );
        return repository.save(
                new User(
                        null,
                        username,
                        passwordEncoder.encode(password),
                        roles
                )
        );
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> ExtendedException.of(ExtendedError.NOT_FOUND)
                );
    }

    public List<User> getByIds(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    public List<User> getAll() {
        return repository.findAll();
    }


    public Boolean existByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(
                        () -> ExtendedException.of(ExtendedError.NOT_FOUND)
                );
    }
}
