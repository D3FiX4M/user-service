package ru.microservices.user_service.internal.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.microservices.user_service.internal.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
