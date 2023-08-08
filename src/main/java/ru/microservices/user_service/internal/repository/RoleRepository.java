package ru.microservices.user_service.internal.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.microservices.user_service.internal.entity.Role;

@Repository
public interface RoleRepository extends JpaRepositoryImplementation<Role, Long> {
}
