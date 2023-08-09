package ru.microservices.user_service.internal.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.microservices.proto.ERoleKey;
import ru.microservices.user_service.internal.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {

    Optional<Role> findByKey(ERoleKey key);
}
