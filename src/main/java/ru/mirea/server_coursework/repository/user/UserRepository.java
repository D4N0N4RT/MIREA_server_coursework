package ru.mirea.server_coursework.repository.user;

import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.AbstractRepository;

import javax.annotation.PostConstruct;

import static ru.mirea.server_coursework.repository.user.UserSpecification.*;

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
