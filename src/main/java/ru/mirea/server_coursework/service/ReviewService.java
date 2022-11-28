package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.ReviewMapper;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.ReviewRepository;

import java.util.List;
import java.util.Objects;

/**
 * Описание класса
 */
@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional(readOnly = true)
    public Review findById(Long id) throws WrongIdException {
        log.info("Find review by id {}", id);
        Review review = reviewRepository.findById(id);
        if (Objects.isNull(review))
            throw new WrongIdException("Неправильный id");
        return review;
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllByAuthor(User author) throws WrongRSQLQueryException {
        log.info("Find reviews by review author {}", author.getUsername());
        return reviewMapper.toReviewDto(reviewRepository.findByAuthor(author));
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllByPostAuthor(User author) throws WrongRSQLQueryException {
        log.info("Find reviews by posts author {}", author.getUsername());
        return reviewMapper.toReviewDto(reviewRepository.findByPostAuthor(author));
    }

    @Transactional()
    public float save(Review review, User seller) throws WrongRSQLQueryException {
        log.info("Create review to post with id {}", review.getPost().getId());
        reviewRepository.create(review);
        List<Review> list = reviewRepository.findByPostAuthor(seller);
        int size = list.size();
        float sum = list.stream().mapToInt(Review::getGrade).sum();
        return sum / size;
    }

    @Transactional()
    public void delete(Review review) {
        log.info("Delete review with id {}", review.getId());
        reviewRepository.delete(review);
    }
}
