package ru.mirea.server_coursework.repository.review;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.AbstractRepository;

import javax.annotation.PostConstruct;
import java.util.List;

import static ru.mirea.server_coursework.repository.review.ReviewSpecification.hasAuthor;
import static ru.mirea.server_coursework.repository.review.ReviewSpecification.hasPostAuthor;

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

    public List<Review> findByAuthor(User author) {
        return findAll(hasAuthor(author));
    }

    public List<Review> findByPostAuthor(User postAuthor) {
        return findAll(hasPostAuthor(postAuthor));
    }
}
