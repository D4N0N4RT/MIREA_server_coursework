package ru.mirea.server_coursework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.exception.WrongIdException;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.ReviewMapper;
import ru.mirea.server_coursework.model.Message;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.ReviewRepository;
import ru.mirea.server_coursework.service.ReviewService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * Описание класса
 */
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    private ReviewService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new ReviewService(reviewRepository, reviewMapper);
    }

    @Test
    public void shouldFindById() throws WrongIdException {
        Review review = Review.builder()
                .content("Test")
                .grade(5)
                .time(LocalDateTime.now()).build();
        User user = User.builder().username("user").id(1L).build();
        Post post = Post.builder().title("Test").id(1L).build();
        review.setPost(post);
        review.setAuthor(user);

        Mockito.when(reviewRepository.findById(any(Long.class))).thenReturn(review);

        Review check = underTest.findById(1L);

        Mockito.verify(reviewRepository).findById(1L);
        Assertions.assertNotNull(check);
    }

    @Test
    public void shouldFindAllByAuthor() throws WrongRSQLQueryException {
        Review review = Review.builder()
                .content("Test")
                .grade(5)
                .time(LocalDateTime.now()).build();
        User user = User.builder().username("user").id(1L).build();
        Post post = Post.builder().title("Test").id(1L).build();
        review.setPost(post);
        review.setAuthor(user);

        Mockito.when(reviewRepository.findByAuthor(any(User.class))).thenReturn(new ArrayList<>(List.of(review)));
        Mockito.when(reviewMapper.toReviewDto(any(ArrayList.class)))
                .thenReturn(new ArrayList<>(List.of(
                        ReviewDTO.builder().content("Test").grade(5)
                                .time(LocalDateTime.now()).build()
                )));

        List<ReviewDTO> check = underTest.getAllByAuthor(user);

        Mockito.verify(reviewRepository).findByAuthor(user);
        Assertions.assertEquals(1, check.size());
    }

    @Test
    public void shouldFindAllByPostAuthor() throws WrongRSQLQueryException {
        Review review = Review.builder()
                .content("Test")
                .grade(5)
                .time(LocalDateTime.now()).build();
        User user = User.builder().username("user").id(1L).build();
        User user2 = User.builder().username("user2").id(2L).build();
        Post post = Post.builder().title("Test").id(1L).user(user2).build();
        review.setPost(post);
        review.setAuthor(user);

        Mockito.when(reviewRepository.findByPostAuthor(any(User.class))).thenReturn(new ArrayList<>(List.of(review)));
        Mockito.when(reviewMapper.toReviewDto(any(ArrayList.class)))
                .thenReturn(new ArrayList<>(List.of(
                        ReviewDTO.builder().content("Test").grade(5)
                                .time(LocalDateTime.now()).build()
                )));

        List<ReviewDTO> check = underTest.getAllByPostAuthor(user);

        Mockito.verify(reviewRepository).findByPostAuthor(user);
        Mockito.verify(reviewMapper).toReviewDto(any(ArrayList.class));
        Assertions.assertEquals(1, check.size());
    }

    @Test
    public void shouldSave() throws WrongRSQLQueryException {
        Review review = Review.builder()
                .content("Test")
                .grade(5)
                .time(LocalDateTime.now()).build();
        User user = User.builder().username("user").id(1L).build();
        User user2 = User.builder().username("user2").id(2L).build();
        Post post = Post.builder().title("Test").id(1L).user(user2).build();
        review.setPost(post);
        review.setAuthor(user);

        underTest.save(review, user2);
        ArgumentCaptor<Review> captor =
                ArgumentCaptor.forClass(Review.class);

        Mockito.verify(reviewRepository).create(captor.capture());
        Review captured = captor.getValue();
        Assertions.assertEquals(captured, review);
    }

}
