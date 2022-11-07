package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostMapper postMapper;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Post> posts = postService.findAllByOrderByPromotionDesc();
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/by_user/sold")
    public ResponseEntity<?> getSoldPostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<Post> posts = postService.findAllByUserAndSold(user, true);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/by_user/available")
    public ResponseEntity<?> getAvailablePostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<Post> posts = postService.findAllByUserAndSold(user, false);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllByCategory(@RequestParam(name="category") @NotNull Category category) {
        List<Post> posts = postService.findAllByCategory(category);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/sort")
    public ResponseEntity<?> getAllSort(@RequestParam(name="field") @NotBlank String field,
                                        @RequestParam(name="order") @NotBlank String order) {
        List<Post> posts = postService.findAllFilter(field, order);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/filter/price/{option}")
    public ResponseEntity<?> getAllFilterPrice(@RequestParam(name="price") double price,
                                               @PathVariable(name="option") @NotBlank String option) {
        List<Post> posts;
        if (Objects.equals(option, "less"))
            posts = postService.findAllByPriceLessThan(price);
        else
            posts = postService.findAllByPriceGreaterThan(price);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/filter/date/{option}")
    public ResponseEntity<?> getAllFilterDate(@RequestParam(name="date")
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                      LocalDate date,
                                              @PathVariable(name="option") String option) {
        List<Post> posts;
        if (Objects.equals(option, "before"))
            posts = postService.findAllByPostingDateIsBefore(date);
        else
            posts = postService.findAllByPostingDateIsAfter(date);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTitle(@RequestParam @NotBlank String title) {
        List<Post> posts = postService.findAllByTitleContainingIgnoreCase(title);
        List<GetPostDTO> dtos = posts.stream().map(Post::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name="id") long id) throws WrongIdException {
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        GetPostDTO dto = post.toDTO();
        return new ResponseEntity<>(dto, HttpStatus.FOUND);
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostDTO dto, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = dto.toPost(user);
        postService.create(post);
        return new ResponseEntity<>("Ваше объявление создано", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name="id") long id, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            postService.delete(post);
            return new ResponseEntity<>("Объявление удалено", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете удалить данное объявление");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable(name="id") long id, @RequestBody @Valid UpdatePostDTO dto, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
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

    @PostMapping("/{id}/buy")
    public ResponseEntity<?> buyPost(@PathVariable(name="id") long id, @RequestParam int grade, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setSold(true);
            postService.update(post);
            User seller = post.getUser();
            seller.setRating(seller.getRating() + grade);
            userService.update(seller);
            postService.updatePostSetRatingForUser(seller.getRating(), seller);
            return new ResponseEntity<>("Объявление куплено", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете купить данное объявление");
        }
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<?> promotePost(@PathVariable(name="id") long id, HttpServletRequest request,
                                         @RequestParam(name="promotion") double promotion)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("Неправльный id"));
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setPromotion(post.getPromotion() + promotion);
            postService.update(post);
            return new ResponseEntity<>("Вы проплатили отображение объявления в топе выдачи", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("Вы не можете проплатить отображение данного объявления");
        }
    }
}
