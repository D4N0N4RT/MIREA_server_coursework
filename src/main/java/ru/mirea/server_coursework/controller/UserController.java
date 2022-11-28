package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.server_coursework.controller.api.UserApi;
import ru.mirea.server_coursework.dto.ShortPostDTO;
import ru.mirea.server_coursework.dto.GetUserDTO;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.PostMapper;
import ru.mirea.server_coursework.mapper.UserMapper;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.ReviewService;
import ru.mirea.server_coursework.service.UserService;

import java.util.List;
import java.util.Objects;

/**
 * Описание класса
 */
@RestController
@AllArgsConstructor
public class UserController implements UserApi {

    private final ReviewService reviewService;
    private final UserService userService;
    private final PostService postService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Override
    public ResponseEntity<?> getById(long id) throws WrongIdException {
        User user = userService.getById(id);
        List<ShortPostDTO> postDTOList = postMapper.toShortPostDto(user.getPosts());
        GetUserDTO dto = userMapper.toUserDto(user, postDTOList);
        return new ResponseEntity<>(dto, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getReviews(long id, String option) throws WrongIdException, WrongRSQLQueryException {
        User user = userService.getById(id);
        if (Objects.equals(option, "received")) {
            List<ReviewDTO> response = reviewService.getAllByPostAuthor(user);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } else if (Objects.equals(option, "written")) {
            List<ReviewDTO> response = reviewService.getAllByAuthor(user);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Неправильный запрос", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getPosts(long id, boolean sold) throws WrongIdException, WrongRSQLQueryException {
        User user = userService.getById(id);
        List<ShortPostDTO> posts = postService.findAllByUserAndSold(user, sold);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }
}
