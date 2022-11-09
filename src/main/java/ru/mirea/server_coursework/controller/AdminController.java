package ru.mirea.server_coursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.UserService;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        userService.delete(user);
        return new ResponseEntity<>("Пользователь удален", HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") long id) throws WrongIdException {
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        postService.delete(post);
        return new ResponseEntity<>("Объявление удалено", HttpStatus.OK);
    }
}
