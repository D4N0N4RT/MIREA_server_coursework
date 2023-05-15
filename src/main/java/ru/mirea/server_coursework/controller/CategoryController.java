package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.server_coursework.controller.api.CategoryApi;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.repository.CategoryRepository;

@RestController
@AllArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryRepository categoryRepository;
    @Override
    public ResponseEntity<?> getAll() throws WrongRSQLQueryException {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
}
