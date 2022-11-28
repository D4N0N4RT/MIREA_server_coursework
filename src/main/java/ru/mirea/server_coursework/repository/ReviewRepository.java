package ru.mirea.server_coursework.repository;

import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;

import javax.annotation.PostConstruct;
import java.util.List;

import static ru.mirea.server_coursework.repository.ReviewSpecification.hasAuthor;
import static ru.mirea.server_coursework.repository.ReviewSpecification.hasPostAuthor;

/**
 * Описание класса
 */
@Repository
public class ReviewRepository extends AbstractRepository<Review, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(Review.class);
    }

    public List<Review> findByAuthor(User author) throws WrongRSQLQueryException {
        return findAll(hasAuthor(author));
    }

    public List<Review> findByPostAuthor(User postAuthor) throws WrongRSQLQueryException {
        return findAll(hasPostAuthor(postAuthor));
    }
}
