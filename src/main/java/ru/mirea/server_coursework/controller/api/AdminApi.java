package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.dto.AuthRequestDTO;
import ru.mirea.server_coursework.dto.RegisterUserDTO;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Описание класса
 */
public interface AdminApi {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/admin",
            produces = { "application/json" }
    )
    ResponseEntity<?> createAdmin(
            @RequestBody @Valid RegisterUserDTO userDTO
    ) throws DuplicateUsernameException, PasswordCheckException;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/admin/users",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllUsers() throws WrongRSQLQueryException;

    /*@RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/users",
            produces = { "application/json" }
    )
    ResponseEntity<?> deleteUser(
            @RequestParam @NotBlank String email
    );*/

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/posts/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> deletePost(
            @PathVariable(name = "id") long id
    ) throws WrongIdException;

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/reviews/{id}",
            produces = { "application/json" }
    )
    ResponseEntity<?> deleteReview(
            @PathVariable(name = "id") long id
    ) throws WrongIdException;
}
