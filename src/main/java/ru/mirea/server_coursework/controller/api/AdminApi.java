package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.exception.WrongIdException;

import javax.validation.constraints.NotBlank;

/**
 * Описание класса
 */
public interface AdminApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/admin/users",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllUsers();

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/users",
            produces = { "application/json" }
    )
    ResponseEntity<?> deleteUser(
            @RequestParam @NotBlank String email
    );

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> deletePost(
            @PathVariable(name = "id") long id
    ) throws WrongIdException;
}
