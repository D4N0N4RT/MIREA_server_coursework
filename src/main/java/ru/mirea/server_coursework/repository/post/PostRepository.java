package ru.mirea.server_coursework.repository.post;

import org.hibernate.Session;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.AbstractRepository;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

import static ru.mirea.server_coursework.repository.post.PostSpecification.*;

@Repository
public class PostRepository extends AbstractRepository<Post, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(Post.class);
    }
    /*@Modifying
    @Query("UPDATE Post p SET p.rating = :rating WHERE p.user = :user")*/
    public void updatePostSetRatingForUser(Integer rating, User user) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Post> update = builder.createCriteriaUpdate(Post.class);
        Root<Post> root = update.from(Post.class);

        update.set("sellerRating", rating).where(builder.equal(root.get("user"), user));

        entityManager.createQuery(update).executeUpdate();
    }

    public List<Post> findByUserAndSold(User user, boolean sold, Sort sort) {
        return findAll(hasUser(user).and(hasSold(sold)), sort);
    }

    public List<Post> findByCategoryAndSold(Category category, boolean sold, Sort sort) {
        return findAll(hasCategory(category).and(hasSold(sold)), sort);
    }

    public List<Post> findAllBySold(boolean sold, Sort sort) {
        return findAll(hasSold(sold), sort);
    }

    public List<Post> findBySoldAndPriceLessThan(boolean sold, double price, Sort sort) {
        return findAll(hasSold(sold).and(hasLowerPrice(price)), sort);
    }

    public List<Post> findBySoldAndPriceGreaterThan(boolean sold, double price, Sort sort) {
        return findAll(hasSold(sold).and(hasGreaterPrice(price)), sort);
    }

    public List<Post> findBySoldAndPostingDateIsBefore(boolean sold, LocalDate postingDate, Sort sort) {
        return findAll(hasSold(sold).and(hasPostingDateBefore(postingDate)), sort);
    }

    public List<Post> findBySoldAndPostingDateIsAfter(boolean sold, LocalDate postingDate, Sort sort) {
        return findAll(hasSold(sold).and(hasPostingDateAfter(postingDate)), sort);
    }

    public List<Post> findBySoldAndTitleContaining(boolean sold, String title, Sort sort) {
        return findAll(hasSold(sold).and(hasTitleLike(title)), sort);
    }
}
