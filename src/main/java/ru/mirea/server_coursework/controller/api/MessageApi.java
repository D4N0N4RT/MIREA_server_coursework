package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Описание класса
 */
public interface MessageApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/messages",
            produces = { "application/json" }
    )
    ResponseEntity<?> getConversation(
            @RequestParam(name="email") @NotBlank String email,
            HttpServletRequest request
    );

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/messages",
            produces = { "application/json" }
    )
    ResponseEntity<?> sendMessage(
            @RequestParam(name="email") @NotBlank String email,
            @RequestBody @Valid @NotBlank(message = "Сообщение не может быть пустым") String content,
            HttpServletRequest request
    );
}
