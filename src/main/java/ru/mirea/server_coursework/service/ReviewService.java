package ru.mirea.server_coursework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.mapper.ReviewMapper;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.review.ReviewRepository;

import java.util.List;

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

    public List<ReviewDTO> getAllByAuthor(User author) {
        return reviewMapper.toReviewDto(reviewRepository.findByAuthor(author));
    }

    public List<ReviewDTO> getAllByPostAuthor(User author) {
        return reviewMapper.toReviewDto(reviewRepository.findByPostAuthor(author));
    }

    public float save(Review review, User author, User seller) {
        reviewRepository.create(review);
        List<Review> list = reviewRepository.findByPostAuthor(seller);
        int size = list.size();
        float sum = list.stream().mapToInt(Review::getGrade).sum();
        return sum / size;
    }
}
