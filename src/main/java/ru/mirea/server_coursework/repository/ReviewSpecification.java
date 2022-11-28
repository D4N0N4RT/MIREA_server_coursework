package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.Review;
import ru.mirea.server_coursework.model.User;

import javax.persistence.criteria.Join;

/**
 * Описание класса
 */
public class ReviewSpecification {

    public static Specification<Review> hasAuthor(User author) {
        return (root, query, criteriaBuilder) -> {
            Join<Review, User> userJoin = root.join("author");
            return criteriaBuilder.equal(userJoin.get("id"), author.getId());
        };
    }

    public static Specification<Review> hasPost(Post post) {
        return (root, query, criteriaBuilder) -> {
            Join<Review, Post> postJoin = root.join("post");
            return criteriaBuilder.equal(postJoin.get("id"), post.getId());
        };
    }

    public static Specification<Review> hasPostAuthor(User postAuthor) {
        return (root, query, criteriaBuilder) -> {
            Join<Review, Post> postJoin = root.join("post");
            Join<Post, User> userJoin = postJoin.join("user");
            return criteriaBuilder.equal(userJoin.get("id"), postAuthor.getId());
        };
    }
}
