package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("UPDATE Post p SET p.rating = :rating WHERE p.user = :user")
    int updatePostSetRatingForUser(@Param("rating") Integer rating, @Param("user") User user);

    List<Post> findAllByUserAndSoldOrderByPromotionDesc(User user, boolean sold);

    List<Post> findAllByCategoryAndSoldOrderByPromotionDescRatingDesc(Category category, boolean sold);

    List<Post> findAllBySoldOrderByPromotionDescRatingDesc(boolean sold);

    List<Post> findAllBySoldOrderByPriceDescPromotionDescRatingDesc(boolean sold);

    List<Post> findAllBySoldOrderByPriceAscPromotionDescRatingDesc(boolean sold);

    List<Post> findAllBySoldOrderByPostingDateDescPromotionDescRatingDesc(boolean sold);

    List<Post> findAllBySoldOrderByPostingDateAscPromotionDescRatingDesc(boolean sold);

    List<Post> findAllBySoldAndPriceLessThanOrderByPromotionDescRatingDesc(boolean sold, double price);

    List<Post> findAllBySoldAndPriceGreaterThanOrderByPromotionDescRatingDesc(boolean sold, double price);

    List<Post> findAllBySoldAndPostingDateIsBeforeOrderByPromotionDescRatingDesc(boolean sold, LocalDate postingDate);

    List<Post> findAllBySoldAndPostingDateIsAfterOrderByPromotionDescRatingDesc(boolean sold, LocalDate postingDate);

    List<Post> findAllBySoldAndTitleContainingIgnoreCaseOrderByPromotionDescRatingDesc(boolean sold, String title);
}
