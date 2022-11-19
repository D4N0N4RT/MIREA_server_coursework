package ru.mirea.server_coursework.controller.api;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.dto.CreatePostDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.model.Category;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Описание класса
 */
public interface PostApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/by_user/sold",
            produces = { "application/json" }
    )
    ResponseEntity<?> getSoldPostsByUser(
            @RequestParam(name = "email") @NotBlank String email
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/by_user/available",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAvailablePostsByUser(
            @RequestParam(name = "email") @NotBlank String email
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/category",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllByCategory(
            @RequestParam(name="category") @NotNull Category category
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/sort",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllSort(
            @RequestParam(name="field") @NotBlank String field,
            @RequestParam(name="order") @NotBlank String order
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/filter/price/{option}",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllFilterPrice(
            @RequestParam(name="price") double price,
            @PathVariable(name="option") @NotBlank String option
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/filter/date/{option}",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllFilterDate(
            @RequestParam(name="date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable(name="option") String option
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/search",
            produces = { "application/json" }
    )
    ResponseEntity<?> searchByTitle(
            @RequestParam @NotBlank String title
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> getById(
            @PathVariable(name="id") long id
    ) throws WrongIdException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/posts",
            produces = { "application/json" }
    )
    ResponseEntity<?> createPost(
            @RequestBody @Valid CreatePostDTO dto,
            HttpServletRequest request
    );

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> deletePost(
            @PathVariable(name="id") long id,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;

    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> editPost(
            @PathVariable(name="id") long id,
            @RequestBody @Valid UpdatePostDTO dto,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/posts/{id}/buy",
            produces = { "application/json" }
    )
    ResponseEntity<?> buyPost(
            @PathVariable(name="id") long id,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/posts/{id}/rate",
            produces = { "application/json" }
    )
    ResponseEntity<?> ratePost(
            @PathVariable(name="id") long id,
            @RequestParam int grade,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/posts/{id}/promote",
            produces = { "application/json" }
    )
    ResponseEntity<?> promotePost(
            @PathVariable(name="id") long id,
            @RequestParam(name="promotion") double promotion,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;
}
