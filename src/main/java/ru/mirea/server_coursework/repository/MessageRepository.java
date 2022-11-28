package ru.mirea.server_coursework.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.model.Message;
import ru.mirea.server_coursework.model.User;

import javax.annotation.PostConstruct;
import java.util.List;

import static ru.mirea.server_coursework.repository.MessageSpecification.*;

@Repository
public class MessageRepository extends AbstractRepository<Message, Long> {

    @Override
    @PostConstruct
    public void init() {
        setClazz(Message.class);
    }
    /*@Query(value = "SELECT mes FROM Message mes WHERE mes.sender = ?1 AND mes.receiver = ?2 " +
            "OR mes.sender = ?2 AND mes.receiver = ?1 ")
    List<Message> findConversation(User user1, User user2, Sort sort);*/

    public List<Message> findConversation(User user1, User user2, Sort sort) throws WrongRSQLQueryException {
        return findAll(hasUsersInSenderAndReceiver(user1, user2), sort);
    }
}
