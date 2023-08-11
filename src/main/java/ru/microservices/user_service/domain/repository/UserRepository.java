package ru.microservices.user_service.domain.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import ru.microservices.user_service.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
