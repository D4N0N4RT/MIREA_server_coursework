package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.server_coursework.controller.api.AdminApi;
import ru.mirea.server_coursework.dto.RegisterUserDTO;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.Role;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.ReviewService;
import ru.mirea.server_coursework.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class AdminController implements AdminApi {
    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createAdmin(@RequestBody @Valid RegisterUserDTO userDTO)
            throws DuplicateUsernameException, PasswordCheckException {
        try {
            userService.loadUserByUsername(userDTO.getUsername());
            throw new DuplicateUsernameException("Администратор с таким именем уже создан");
        } catch(UsernameNotFoundException ex) {
            userService.checkDTO(userDTO);
            User user = userDTO.toUser();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.ADMIN);
            userService.create(user);
            return new ResponseEntity<>("Новый администратор создан", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAllUsers() throws WrongRSQLQueryException {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /*public ResponseEntity<?> deleteUser(@RequestParam @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        userService.delete(user);
        return new ResponseEntity<>("Пользователь удален", HttpStatus.OK);
    }*/

    public ResponseEntity<?> deletePost(@PathVariable(name = "id") long id) throws WrongIdException {
        Post post = postService.findById(id);
        postService.delete(post);
        return new ResponseEntity<>("Объявление удалено", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteReview(long id) throws WrongIdException {
        Review review = reviewService.findById(id);
        reviewService.delete(review);
        return new ResponseEntity<>("Отзыв удален", HttpStatus.OK);
    }
}
