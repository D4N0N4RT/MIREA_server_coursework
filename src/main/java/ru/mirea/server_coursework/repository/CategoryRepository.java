package ru.mirea.server_coursework.repository;

import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.Category;

import javax.annotation.PostConstruct;

@Repository
public class CategoryRepository extends AbstractRepository<Category, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(Category.class);
    }
}
