package ru.mirea.server_coursework.repository;

import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.User;

import javax.annotation.PostConstruct;

import static ru.mirea.server_coursework.repository.UserSpecification.*;

@Repository
public class UserRepository extends AbstractRepository<User, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(User.class);
    }

    public User findByUsername(String username) {
        return findOne(hasUsername(username));
    }
}
