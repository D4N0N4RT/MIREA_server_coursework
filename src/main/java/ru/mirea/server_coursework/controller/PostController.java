package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.server_coursework.controller.api.PostApi;
import ru.mirea.server_coursework.dto.BasicApiResponse;
import ru.mirea.server_coursework.dto.CreatePostDTO;
import ru.mirea.server_coursework.dto.CreateReviewDTO;
import ru.mirea.server_coursework.dto.FullPostDTO;
import ru.mirea.server_coursework.dto.ShortPostDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.PostMapper;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.CategoryRepository;
import ru.mirea.server_coursework.security.JwtTokenProvider;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.ReviewService;
import ru.mirea.server_coursework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@RestController
public class PostController implements PostApi {
    private final PostService postService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostMapper postMapper;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<?> getAll() throws WrongRSQLQueryException {
        List<ShortPostDTO> posts = postService.findAllByOrderByPromotionDescRatingDesc();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /*public ResponseEntity<?> getAllSort(@RequestParam(name="field") @NotBlank String field,
                                        @RequestParam(name="order") @NotBlank String order) throws WrongRSQLQueryException {
        List<ShortPostDTO> posts = postService.findAllSorted(field, order);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }*/

    public ResponseEntity<?> getAllFilter(@RequestParam(name="query", required = false) String rsqlQuery,
                                          @RequestParam(name="sort", required = false) int sortOption) throws
            WrongRSQLQueryException {
        List<ShortPostDTO> posts = postService.findAllByRsqlQuery(rsqlQuery, sortOption);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    /*public ResponseEntity<?> searchByTitle(@RequestParam @NotBlank String title) throws WrongRSQLQueryException {
        List<ShortPostDTO> posts = postService.findAllByTitleContainingIgnoreCase(title);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }*/

    public ResponseEntity<?> getById(@PathVariable(name="id") long id) throws WrongIdException {
        Post post = postService.findById(id);
        FullPostDTO postDto = postMapper
                .toFullPostDto(post);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostDTO dto, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Category category = categoryRepository.findById(dto.getCategoryId());
        Post post = dto.toPost(user, category);
        postService.create(post);
        return new ResponseEntity<>(new BasicApiResponse(201, "Ваше объявление создано"), HttpStatus.CREATED);
    }

    /*public ResponseEntity<?> deletePost(@PathVariable(name="id") long id, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            postService.delete(post);
            return new ResponseEntity<>("Объявление удалено", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете удалить данное объявление");
        }
    }*/

    public ResponseEntity<?> editPost(@PathVariable(name="id") long id, @RequestBody @Valid UpdatePostDTO dto,
                                      HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            /*if (dto.getPrice() < 0)
                throw new PriceValidException("Ошибка валидации, проверьте введенные данные\nОшибка: Цена должна быть не меньше 1");*/
            postMapper.updatePostFromDto(dto, post);
            postService.update(post);
            return new ResponseEntity<>(new BasicApiResponse(200, "Данные объявления обновлены"), HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете изменить данное объявление");
        }
    }

    public ResponseEntity<?> buyPost(@PathVariable(name="id") long id, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setSold(true);
            post.setBuyerId(user.getId());
            postService.update(post);
            return new ResponseEntity<>(new BasicApiResponse(200, "Объявление куплено"), HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете купить данное объявление");
        }
    }

    @Override
    public ResponseEntity<?> reviewPost(long id, @Valid CreateReviewDTO dto, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException, WrongRSQLQueryException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (!post.isSold() && Objects.equals(post.getBuyerId(), user.getId())) {
            Review review = dto.toReview(post, user);
            User seller = post.getUser();
            float newRating = reviewService.save(review, seller);
            postService.updatePostSetRatingForUser(newRating, seller);
            return new ResponseEntity<>(new BasicApiResponse(201, "Отзыв оставлен"), HttpStatus.CREATED);
        } else {
            throw new WrongAuthorityException("Вы не можете оставить отзыв");
        }
    }

    public ResponseEntity<?> promotePost(@PathVariable(name="id") long id,
                                         @RequestParam(name="promotion") double promotion,
                                         HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setPromotion(post.getPromotion() + promotion);
            postService.update(post);
            return new ResponseEntity<>(new BasicApiResponse(200, "Вы проплатили отображение объявления в топе выдачи"),
                    HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете проплатить отображение данного объявления");
        }
    }
}
