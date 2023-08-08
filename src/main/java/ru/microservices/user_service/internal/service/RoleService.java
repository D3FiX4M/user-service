package ru.microservices.user_service.internal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.microservices.user_service.internal.entity.Role;
import ru.microservices.user_service.internal.repository.RoleRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repository;


    public List<Role> get(Collection<Long> ids) {
        return repository.findAllById(ids);
    }
}
