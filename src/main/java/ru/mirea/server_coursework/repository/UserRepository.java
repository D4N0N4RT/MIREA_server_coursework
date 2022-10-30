package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.server_coursework.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
