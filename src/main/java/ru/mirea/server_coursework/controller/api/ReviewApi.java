package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mirea.server_coursework.dto.CreateReviewDTO;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Описание класса
 */
public interface ReviewApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/reviews/user/leaved",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllByAuthor(
            @RequestParam(name = "email") @NotBlank String authorEmail
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/reviews/user/received",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAllByPostAuthor(
            @RequestParam(name = "email") @NotBlank String postAuthorEmail
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/reviews/{postId}",
            produces = { "application/json" }
    )
    ResponseEntity<?> leaveReview(
            @PathVariable long postId, HttpServletRequest request, CreateReviewDTO dto
    ) throws WrongIdException, WrongAuthorityException;
}
