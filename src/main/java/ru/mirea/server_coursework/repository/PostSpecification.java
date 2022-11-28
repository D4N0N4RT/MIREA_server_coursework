package ru.mirea.server_coursework.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.mirea.server_coursework.model.Category;
import ru.mirea.server_coursework.model.Post;
import ru.mirea.server_coursework.model.User;

import javax.persistence.criteria.Join;
import java.time.LocalDate;

public class PostSpecification {

    public static Specification<Post> hasUser(User user) {
        return (root, query, criteriaBuilder) -> {
            Join<Post, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("id"), user.getId());
        };
    }

    public static Specification<Post> hasSold(boolean isSold) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sold"), isSold);
    }

    public static Specification<Post> hasCategory(Category category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Post> hasLowerPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("price"), price);
    }

    public static Specification<Post> hasGreaterPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), price);
    }

    public static Specification<Post> hasPostingDateBefore(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.<LocalDate>get("postingDate"), date);
    }

    public static Specification<Post> hasPostingDateAfter(LocalDate date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.<LocalDate>get("postingDate"), date);
    }

    public static Specification<Post> hasTitleLike(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%");
    }
}
