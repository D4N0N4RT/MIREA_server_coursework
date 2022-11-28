package ru.mirea.server_coursework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.ReviewDTO;
import ru.mirea.server_coursework.model.Review;

import java.util.Collection;
import java.util.List;

/**
 * Описание класса
 */
@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "reviewAuthor", source = "review.author.username")
    @Mapping(target = "postId", source = "review.post.id")
    @Mapping(target = "postAuthor", source = "review.post.user.username")
    ReviewDTO toReviewDto(Review review);

    @Mapping(target = "reviewAuthor", source = "review.author.username")
    @Mapping(target = "postId", source = "review.post.id")
    @Mapping(target = "postAuthor", source = "review.post.user.username")
    List<ReviewDTO> toReviewDto(Collection<Review> reviews);
}
