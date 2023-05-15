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
        componentModel = "spring",
        uses = {PostMapper.class, UserMapper.class})
public interface ReviewMapper {

    @Mapping(target = "reviewAuthor", source = "review.author")
    @Mapping(target = "post", source = "review.post")
    @Mapping(target = "postAuthor", source = "review.post.user")
    ReviewDTO toReviewDto(Review review);

    @Mapping(target = "reviewAuthor", source = "review.author")
    @Mapping(target = "post", source = "review.post")
    @Mapping(target = "postAuthor", source = "review.post.user")
    List<ReviewDTO> toReviewDto(Collection<Review> reviews);
}
