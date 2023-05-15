package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.dto.CreatePostDTO;
import ru.mirea.server_coursework.dto.CreateReviewDTO;
import ru.mirea.server_coursework.dto.UpdatePostDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Описание класса
 */
public interface PostApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAll() throws WrongRSQLQueryException;

    /*@RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/sort",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllSort(
            @RequestParam(name="field") @NotBlank String field,
            @RequestParam(name="order") @NotBlank String order
    ) throws WrongRSQLQueryException;*/

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/filter",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllFilter(
            @RequestParam(name="query") String rsqlQuery,
            @RequestParam(name="sort") int sortOption
    ) throws WrongRSQLQueryException;

    /*@RequestMapping(
            method = RequestMethod.GET,
            value = "/posts/search",
            produces = { "application/json" }
    )
    ResponseEntity<?> searchByTitle(
            @RequestParam @NotBlank String title
    ) throws WrongRSQLQueryException;*/

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

    /*@RequestMapping(
            method = RequestMethod.DELETE,
            value = "/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> deletePost(
            @PathVariable(name="id") long id,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;*/

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
            method = RequestMethod.PATCH,
            value = "/posts/{id}/buy",
            produces = { "application/json" }
    )
    ResponseEntity<?> buyPost(
            @PathVariable(name="id") long id,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/posts/{id}/review",
            produces = { "application/json" }
    )
    ResponseEntity<?> reviewPost(
            @PathVariable(name="id") long id,
            @Valid CreateReviewDTO dto,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException, WrongRSQLQueryException;

    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/posts/{id}/promote",
            produces = { "application/json" }
    )
    ResponseEntity<?> promotePost(
            @PathVariable(name="id") long id,
            @RequestParam(name="promotion") double promotion,
            HttpServletRequest request
    ) throws WrongIdException, WrongAuthorityException;
}
