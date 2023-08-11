package ru.microservices.user_service.domain.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.microservices.user_service.ERoleKey;
import ru.microservices.user_service.domain.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {

    Optional<Role> findByKey(ERoleKey key);
}
