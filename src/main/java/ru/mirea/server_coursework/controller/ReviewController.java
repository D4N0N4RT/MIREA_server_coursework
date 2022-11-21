package ru.mirea.server_coursework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.mirea.server_coursework.controller.api.ReviewApi;
import ru.mirea.server_coursework.dto.CreateReviewDTO;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.security.JwtTokenProvider;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.ReviewService;
import ru.mirea.server_coursework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * Описание класса
 */
@Controller
public class ReviewController implements ReviewApi {

    private final ReviewService reviewService;
    private final PostService postService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ReviewController(ReviewService reviewService, PostService postService, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.reviewService = reviewService;
        this.postService = postService;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<?> getAllByAuthor(String authorEmail) {
        User author = (User) userService.loadUserByUsername(authorEmail);
        List<ReviewDTO> reviews = reviewService.getAllByAuthor(author);
        return new ResponseEntity<>(reviews, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllByPostAuthor(String postAuthorEmail) {
        User author = (User) userService.loadUserByUsername(postAuthorEmail);
        List<ReviewDTO> reviews = reviewService.getAllByPostAuthor(author);
        return new ResponseEntity<>(reviews, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> leaveReview(long postId, HttpServletRequest request, @Valid CreateReviewDTO dto)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(postId);
        if (!Objects.equals(post.getBuyerId(), user.getId())) {
            Review review = dto.toReview(post, user);
            User seller = post.getUser();
            float newRating = reviewService.save(review, user, seller);
            postService.updatePostSetRatingForUser(newRating, seller);
            return new ResponseEntity<>("Отзыв оставлен", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете оставить отзыв");
        }
    }
}
