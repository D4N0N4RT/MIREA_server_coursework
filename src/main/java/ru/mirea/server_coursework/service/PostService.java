package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.dto.ShortPostDTO;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.PostMapper;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.PostRepository;
import ru.mirea.server_coursework.repository.PostSpecification;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    Sort sortPromotionAndRating = Sort.by(List.of(
            new Sort.Order(Sort.Direction.DESC, "promotion"),
            new Sort.Order(Sort.Direction.DESC, "sellerRating")));
    Sort sortPromotion = Sort.by(List.of(
            new Sort.Order(Sort.Direction.DESC, "promotion")));

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public Post findById(long id) throws WrongIdException {
        log.info("Find post by id = {}", id);
        Post post = postRepository.findById(id);
        if (Objects.isNull(post))
            throw new WrongIdException("Неправильный id");
        return post;
    }

    @Transactional(readOnly = true)
    public List<ShortPostDTO> findAllByOrderByPromotionDescRatingDesc() throws WrongRSQLQueryException {
        log.info("Find all posts w/o order or filter");
        return postMapper.toShortPostDto(postRepository.findAllBySold(false, sortPromotionAndRating));
    }

    @Transactional(readOnly = true)
    public List<ShortPostDTO> findAllSorted(String field, String option) throws WrongRSQLQueryException {
        log.info("Find all posts sorted by {} {}ending", field, option);
        List<ShortPostDTO> posts;
        Sort sort;
        if (Objects.equals(field, "price") && Objects.equals(option, "asc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.ASC, "price"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "sellerRating")
            ));
        else if (Objects.equals(field, "price") && Objects.equals(option, "desc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.DESC, "price"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "sellerRating")
            ));
        else if (Objects.equals(field, "postingDate") && Objects.equals(option, "asc"))
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.ASC, "postingDate"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "sellerRating")
            ));
        else
            sort = Sort.by(List.of(
                    new Sort.Order(Sort.Direction.DESC, "postingDate"),
                    new Sort.Order(Sort.Direction.DESC, "promotion"),
                    new Sort.Order(Sort.Direction.DESC, "sellerRating")
            ));
        posts = postMapper.toShortPostDto(postRepository.findAllBySold(false, sort));
        return posts;
    }

    @Transactional(readOnly = true)
    public List<ShortPostDTO> findAllByCategory(Category category) throws WrongRSQLQueryException {
        log.info("Find all posts with certain category {}", category.name());
        return postMapper.toShortPostDto(postRepository.findByCategoryAndSold(category, false, sortPromotionAndRating));
    }

    @Transactional(readOnly = true)
    public List<ShortPostDTO> findAllByUserAndSold(User user, boolean sold) throws WrongRSQLQueryException {
        log.info("Find all posts by user {} and which {} sold", user.getUsername(), sold ? "are" : "are not");
        return postMapper.toShortPostDto(postRepository.findByUserAndSold(user, sold, sortPromotionAndRating));
    }

    public List<ShortPostDTO> findAllByRsqlQuery(String rsqlQuery) throws WrongRSQLQueryException {
        log.info("Find all posts filtered by RSQL query {}", rsqlQuery);
        return postMapper.toShortPostDto(postRepository.findAllByQuery(PostSpecification.hasSold(false), rsqlQuery, sortPromotionAndRating));
    }

    @Transactional(readOnly = true)
    public List<ShortPostDTO> findAllByTitleContainingIgnoreCase(String title) throws WrongRSQLQueryException {
        log.info("Find all by title containing string '{}'", title);
        return postMapper.toShortPostDto(postRepository.findBySoldAndTitleContaining(false,
                title, sortPromotionAndRating));
    }

    @Transactional
    public void updatePostSetRatingForUser(Float rating, User user) {
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
