package ru.mirea.server_coursework.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

public interface CategoryApi {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/categories",
            produces = { "application/json" }
    )
    ResponseEntity<?> getAll() throws WrongRSQLQueryException;
}
