package ru.mirea.server_coursework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.UserRepository;
import ru.mirea.server_coursework.service.UserService;


import static org.mockito.ArgumentMatchers.any;

/**
 * Описание класса
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void shouldFindById() throws WrongIdException {
        User user = User.builder().username("user").id(1L).build();

        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(user);

        User check = underTest.getById(1L);

        Mockito.verify(userRepository).findById(1L);
        Assertions.assertNotNull(check);
    }

    @Test
    public void shouldLoadUserByUsername() throws UsernameNotFoundException {
        User user = User.builder().username("user").id(1L).build();

        Mockito.when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        User check = (User) underTest.loadUserByUsername("user");

        Mockito.verify(userRepository).findByUsername("user");
        Assertions.assertNotNull(check);
    }

    @Test
    public void shouldSave() {
        User user = User.builder().username("user").id(1L).build();

        underTest.create(user);
        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        Mockito.verify(userRepository).create(captor.capture());
        User captured = captor.getValue();
        Assertions.assertEquals(captured, user);
    }

}
