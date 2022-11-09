package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.dto.IUserDTO;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.user.UserRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (userRepository.findByUsername(username) == null) {
            log.warn("There is no user with username - {}", username);
            throw new UsernameNotFoundException("Пользователя с таким именем не существует");
        }
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void create(User user) {
        log.info("Create new user with username {}", user.getUsername());
        userRepository.create(user);
    }

    @Transactional
    public void delete(User user) {
        log.info("Delete user with username {}", user.getUsername());
        userRepository.delete(user);
    }

    @Transactional
    public void update(User user) {
        log.info("Update profile info of user with username {}", user.getUsername());
        userRepository.create(user);
    }

    public String checkDTO(IUserDTO dto) throws PasswordCheckException {
        if (dto.getPassword() != null) {
            String CHECK = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+)[a-zA-Z0-9_-]{8,}$";
            Pattern pattern = Pattern.compile(CHECK);
            Matcher matcher = pattern.matcher(dto.getPassword());
            if (!matcher.matches()) {
                throw new PasswordCheckException("Пароль должен содержать как миниму одну строчную букву, " +
                        "одну заглавную букву и одну цифру, а также быть не менне 8 символов в длину.");
            }
            return passwordEncoder.encode(dto.getPassword());
        }
        return null;
    }
}

