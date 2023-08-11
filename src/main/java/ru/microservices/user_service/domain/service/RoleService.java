package ru.microservices.user_service.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.microservices.user_service.ERoleKey;
import ru.microservices.user_service.core.exception.ExtendedError;
import ru.microservices.user_service.core.exception.ExtendedException;
import ru.microservices.user_service.domain.entity.Role;
import ru.microservices.user_service.domain.repository.RoleRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;


    public List<Role> get(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    public Role getByKey(ERoleKey key) {
        return repository.findByKey(key)
                .orElseThrow(
                        () -> ExtendedException.of(ExtendedError.NOT_FOUND)
                );
    }
}
