package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.controller.api.PostApi;
import ru.mirea.server_coursework.dto.CreatePostDTO;
import ru.mirea.server_coursework.dto.GetPostDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.mapper.PostMapper;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.security.JwtTokenProvider;
import ru.mirea.server_coursework.service.PostService;
import ru.mirea.server_coursework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Controller
public class PostController implements PostApi {
    private final PostService postService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostMapper postMapper;

    public ResponseEntity<?> getAll() {
        List<GetPostDTO> posts = postService.findAllByOrderByPromotionDesc();
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    public ResponseEntity<?> getSoldPostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<GetPostDTO> posts = postService.findAllByUserAndSold(user, true);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    public ResponseEntity<?> getAvailablePostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<GetPostDTO> posts = postService.findAllByUserAndSold(user, false);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    /*public ResponseEntity<?> getAllByCategory(@RequestParam(name="category") @NotNull Category category) {
        List<GetPostDTO> posts = postService.findAllByCategory(category);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }*/

    public ResponseEntity<?> getAllSort(@RequestParam(name="field") @NotBlank String field,
                                        @RequestParam(name="order") @NotBlank String order) {
        List<GetPostDTO> posts = postService.findAllSorted(field, order);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    public ResponseEntity<?> getAllFilter(@RequestParam(name="q") String rsqlQuery) {
        List<GetPostDTO> posts = postService.findAllByRsqlQuery(rsqlQuery);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    public ResponseEntity<?> searchByTitle(@RequestParam @NotBlank String title) {
        List<GetPostDTO> posts = postService.findAllByTitleContainingIgnoreCase(title);
        return new ResponseEntity<>(posts, HttpStatus.FOUND);
    }

    public ResponseEntity<?> getById(@PathVariable(name="id") long id) throws WrongIdException {
        Post post = postService.findById(id);
        GetPostDTO postDto = postMapper
                .toPostDto(post);
        return new ResponseEntity<>(postDto, HttpStatus.FOUND);
    }

    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostDTO dto, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = dto.toPost(user);
        postService.create(post);
        return new ResponseEntity<>("Ваше объявление создано", HttpStatus.CREATED);
    }

    public ResponseEntity<?> deletePost(@PathVariable(name="id") long id, HttpServletRequest request)
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
    }

    public ResponseEntity<?> editPost(@PathVariable(name="id") long id, @RequestBody @Valid UpdatePostDTO dto, HttpServletRequest request)
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
            return new ResponseEntity<>("Данные объявления обновлены", HttpStatus.OK);
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
            return new ResponseEntity<>("Объявление куплено", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете купить данное объявление");
        }
    }

    /*public ResponseEntity<?> ratePost(@PathVariable(name="id") long id, @RequestParam int grade, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id);
        if (!Objects.equals(post.getBuyerId(), user.getId())) {
            User seller = post.getUser();
            seller.setRating(seller.getRating() + grade);
            userService.update(seller);
            postService.updatePostSetRatingForUser(seller.getRating(), seller);
            return new ResponseEntity<>("Объявление оценено", HttpStatus.OK);
        }
        else {
            throw new WrongAuthorityException("Вы не можете оценить данное объявление");
        }
    }*/

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
            return new ResponseEntity<>("Вы проплатили отображение объявления в топе выдачи", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете проплатить отображение данного объявления");
        }
    }
}
