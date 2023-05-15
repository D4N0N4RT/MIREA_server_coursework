package ru.mirea.server_coursework.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.mirea.server_coursework.dto.BasicApiResponse;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.exception.TokenRefreshException;
import ru.mirea.server_coursework.exception.WrongAuthorityException;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { WrongIdException.class, WrongRSQLQueryException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new BasicApiResponse(404, ex.getMessage()) , new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { AuthenticationException.class,
            UsernameNotFoundException.class })
    protected ResponseEntity<Object> handleAuthExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new BasicApiResponse(401, ex.getMessage()), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {TokenRefreshException.class})
    protected ResponseEntity<Object> handleForbidden(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new BasicApiResponse(403, ex.getMessage()), new HttpHeaders(),
                HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = { WrongAuthorityException.class, PasswordCheckException.class,
            DuplicateUsernameException.class })
    protected ResponseEntity<Object> handleConflictExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new BasicApiResponse(409, ex.getMessage()), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        StringBuilder error = new StringBuilder("Ошибка валидации, проверьте введенные данные\nОшибка: "
                + errors.get(0).getDefaultMessage());
        for (int i = 1; i < errors.size(); i++) {
            error.append(", ").append(errors.get(i).getDefaultMessage());
        }
        return handleExceptionInternal(ex, new BasicApiResponse(400, error.toString()), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
