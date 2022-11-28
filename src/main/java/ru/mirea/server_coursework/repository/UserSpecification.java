package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.mirea.server_coursework.model.User;

public class UserSpecification {

    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
    }
}
