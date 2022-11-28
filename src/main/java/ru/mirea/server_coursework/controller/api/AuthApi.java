package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mirea.server_coursework.dto.AuthRequestDTO;
import ru.mirea.server_coursework.dto.RegisterUserDTO;
import ru.mirea.server_coursework.dto.UpdateUserDTO;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Описание класса
 */
public interface AuthApi {
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/auth/register",
            produces = { "application/json" }
    )
    ResponseEntity<?> register(
            @RequestBody @Valid RegisterUserDTO userDTO
    ) throws DuplicateUsernameException, PasswordCheckException;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/auth/login",
            produces = { "application/json" }
    )
    ResponseEntity<?> authenticate(
            @RequestBody @Valid AuthRequestDTO request
    ) throws UsernameNotFoundException;

    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/auth/edit"
    )
    ResponseEntity<?> editProfile(
            @RequestBody @Valid UpdateUserDTO dto,
            HttpServletRequest request
    ) throws PasswordCheckException;

    /*@RequestMapping(
            method = RequestMethod.POST,
            value = "/auth/logout"
    )
    void logout(
            HttpServletRequest request,
            HttpServletResponse response
    );*/
}
