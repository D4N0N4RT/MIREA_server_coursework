package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.post.PostRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;

    Sort sortPromotionAndRating = Sort.by(List.of(
            new Sort.Order(Sort.Direction.DESC, "promotion"),
            new Sort.Order(Sort.Direction.DESC, "rating")));
    Sort sortPromotion = Sort.by(List.of(
            new Sort.Order(Sort.Direction.DESC, "promotion")));

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(long id) {
        log.info("Find post by id = {}", id);
        return postRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByOrderByPromotionDesc() {
        log.info("Find all posts w/o order or filter");
        return postRepository.findAllBySold(false, sortPromotion);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllSorted(String field, String option) {
        log.info("Find all posts sorted by {} {}ending", field, option);
        List<Post> posts;
        Sort sort;
        if (Objects.equals(field, "price") && Objects.equals(option, "asc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.ASC, "price"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "rating")
            ));
        else if (Objects.equals(field, "price") && Objects.equals(option, "desc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.DESC, "price"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "rating")
            ));
        else if (Objects.equals(field, "postingDate") && Objects.equals(option, "asc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.ASC, "postingDate"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "rating")
            ));
        else
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.DESC, "postingDate"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "rating")
            ));
        posts = postRepository.findAllBySold(false, sort);
        return posts;
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByCategory(Category category) {
        log.info("Find all posts with certain category {}", category.name());
        return postRepository.findByCategoryAndSold(category, false, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByUserAndSold(User user, boolean sold) {
        log.info("Find all posts by user {} and which {} sold", user.getUsername(), sold ? "are" : "are not");
        return postRepository.findByUserAndSold(user, sold, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPriceLessThan(double price) {
        log.info("Find all posts with price less than {}", price);
        return postRepository.findBySoldAndPriceLessThan(false, price, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPriceGreaterThan(double price) {
        log.info("Find all posts with price greater than {}", price);
        return postRepository.findBySoldAndPriceGreaterThan(false, price, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPostingDateIsBefore(LocalDate postingDate) {
        log.info("Find all posts with posting date is before {}", postingDate);
        return postRepository.findBySoldAndPostingDateIsBefore(false, postingDate, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPostingDateIsAfter(LocalDate postingDate) {
        log.info("Find all posts with posting date is after {}", postingDate);
        return postRepository.findBySoldAndPostingDateIsAfter(false, postingDate, sortPromotionAndRating);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByTitleContainingIgnoreCase(String title) {
        log.info("Find all by title containing string '{}'", title);
        return postRepository.findBySoldAndTitleContaining(false, title, sortPromotionAndRating);
    }

    @Transactional
    public void updatePostSetRatingForUser(Integer rating, User user) {
        log.info("Update rating of posts from user - {}", user.getUsername());
        postRepository.updatePostSetRatingForUser(rating, user);
    }

    @Transactional
    public void create(Post post) {
        log.info("Create new post");
        postRepository.create(post);
    }

    @Transactional
    public void delete(Post post) {
        log.info("Delete post with id {}", post.getId());
        postRepository.delete(post);
    }

    @Transactional
    public void update(Post post) {
        log.info("Update post with id {}", post.getId());
        postRepository.create(post);
    }
}
