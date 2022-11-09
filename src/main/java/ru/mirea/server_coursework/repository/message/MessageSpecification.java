package ru.mirea.server_coursework.repository.message;

import org.springframework.data.jpa.domain.Specification;
import ru.mirea.server_coursework.model.Message;
import ru.mirea.server_coursework.model.User;

import javax.persistence.criteria.Join;

public class MessageSpecification {

    public static Specification<Message> hasUsersInSenderAndReceiver(User user1, User user2) {
        return ((root, query, criteriaBuilder) -> {
            Join<Message, User> senderJoin = root.join("sender");
            Join<Message, User> receiverJoin = root.join("receiver");
            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(senderJoin.get("id"), user1.getId()),
                            criteriaBuilder.equal(senderJoin.get("id"), user2.getId())
                    ),
                    criteriaBuilder.or(
                            criteriaBuilder.equal(receiverJoin.get("id"), user1.getId()),
                            criteriaBuilder.equal(receiverJoin.get("id"), user2.getId())
                    )
            );
        });
    }
}
