package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.PostRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;

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
        return postRepository.findAllBySoldOrderByPromotionDescRatingDesc(false);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllFilter(String field, String option) {
        log.info("Find all posts with filter by {}", field);
        List<Post> posts;
        if (Objects.equals(field, "price") && Objects.equals(option, "asc"))
            posts = postRepository.findAllBySoldOrderByPriceAscPromotionDescRatingDesc(false);
        else if (Objects.equals(field, "price") && Objects.equals(option, "desc"))
            posts = postRepository.findAllBySoldOrderByPriceDescPromotionDescRatingDesc(false);
        else if (Objects.equals(field, "postingDate") && Objects.equals(option, "asc"))
            posts = postRepository.findAllBySoldOrderByPostingDateAscPromotionDescRatingDesc(false);
        else
            posts = postRepository.findAllBySoldOrderByPostingDateDescPromotionDescRatingDesc(false);
        return posts;
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByCategory(Category category) {
        log.info("Find all posts with certain category {}", category.name());
        return postRepository.findAllByCategoryAndSoldOrderByPromotionDescRatingDesc(category, false);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByUserAndSold(User user, boolean sold) {
        log.info("Find all posts by user {} and which {} sold", user.getUsername(), sold ? "are" : "are not");
        return postRepository.findAllByUserAndSoldOrderByPromotionDesc(user, sold);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPriceLessThan(double price) {
        log.info("Find all posts with price less than {}", price);
        return postRepository.findAllBySoldAndPriceLessThanOrderByPromotionDescRatingDesc(false, price);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPriceGreaterThan(double price) {
        log.info("Find all posts with price greater than {}", price);
        return postRepository.findAllBySoldAndPriceGreaterThanOrderByPromotionDescRatingDesc(false, price);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPostingDateIsBefore(LocalDate postingDate) {
        log.info("Find all posts with posting date is before {}", postingDate);
        return postRepository.findAllBySoldAndPostingDateIsBeforeOrderByPromotionDescRatingDesc(false, postingDate);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByPostingDateIsAfter(LocalDate postingDate) {
        log.info("Find all posts with posting date is after {}", postingDate);
        return postRepository.findAllBySoldAndPostingDateIsAfterOrderByPromotionDescRatingDesc(false, postingDate);
    }

    @Transactional(readOnly = true)
    public List<Post> findAllByTitleContainingIgnoreCase(String title) {
        log.info("Find all by title containing string '{}'", title);
        return postRepository.findAllBySoldAndTitleContainingIgnoreCaseOrderByPromotionDescRatingDesc(false, title);
    }

    @Transactional
    public int updatePostSetRatingForUser(Integer rating, User user) {
        log.info("Update rating of posts from user - {}", user.getUsername());
        return postRepository.updatePostSetRatingForUser(rating, user);
    }

    @Transactional
    public void create(Post post) {
        log.info("Create new post");
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        log.info("Delete post with id {}", post.getId());
        postRepository.delete(post);
    }

    @Transactional
    public void update(Post post) {
        log.info("Update post with id {}", post.getId());
        postRepository.save(post);
    }
}
