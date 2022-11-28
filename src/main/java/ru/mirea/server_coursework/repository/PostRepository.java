package ru.mirea.server_coursework.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.mirea.server_coursework.repository.PostSpecification.*;

@Repository
public class PostRepository extends AbstractRepository<Post, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(Post.class);
    }
    /*@Modifying
    @Query("UPDATE Post p SET p.rating = :rating WHERE p.user = :user")*/
    public void updatePostSetRatingForUser(Float rating, User user) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Post> update = builder.createCriteriaUpdate(Post.class);
        Root<Post> root = update.from(Post.class);

        update.set("sellerRating", rating).where(builder.equal(root.get("user"), user));

        entityManager.createQuery(update).executeUpdate();
    }

    public List<Post> findByUserAndSold(User user, boolean sold, Sort sort) throws WrongRSQLQueryException {
        return findAll(hasUser(user).and(hasSold(sold)), sort);
    }

    public List<Post> findByCategoryAndSold(Category category, boolean sold, Sort sort) throws WrongRSQLQueryException {
        return findAll(hasCategory(category).and(hasSold(sold)), sort);
    }

    public List<Post> findAllBySold(boolean sold, Sort sort) throws WrongRSQLQueryException {
        return findAll(hasSold(sold), sort);
    }


    public List<Post> findBySoldAndTitleContaining(boolean sold, String title, Sort sort) throws WrongRSQLQueryException {
        return findAll(hasSold(sold).and(hasTitleLike(title)), sort);
    }
}
