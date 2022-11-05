package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        log.info("Find all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Find user with username {}", username);
        if (userRepository.findByUsername(username).isEmpty()) {
            log.warn("There is no user with username - {}", username);
            throw new UsernameNotFoundException("Пользователя с таким именем не существует");
        }
        return userRepository.findByUsername(username).get();
    }

    @Transactional
    public void create(User user) {
        log.info("Create new user with username {}", user.getUsername());
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        log.info("Delete user with username {}", user.getUsername());
        userRepository.delete(user);
    }

    @Transactional
    public void update(User user) {
        log.info("Update profile info of user with username {}", user.getUsername());
        userRepository.save(user);
    }
}

