package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

/**
 * Описание класса
 */
public interface UserApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> getById(
            @PathVariable(name = "id") long id
    ) throws WrongIdException;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{id}/reviews",
            produces = { "application/json" }
    )
    ResponseEntity<?> getReviews(
            @PathVariable(name = "id") long id, @RequestParam(name = "option") String option
    ) throws WrongIdException, WrongRSQLQueryException;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{id}/posts",
            produces = { "application/json" }
    )
    ResponseEntity<?> getPosts(
            @PathVariable(name = "id") long id, @RequestParam(name = "sold") boolean sold
    ) throws WrongIdException, WrongRSQLQueryException;
}
