package ru.mirea.server_coursework.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mirea.server_coursework.model.Message;
import ru.mirea.server_coursework.model.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT mes FROM Message mes WHERE mes.sender = ?1 AND mes.receiver = ?2 " +
            "OR mes.sender = ?2 AND mes.receiver = ?1 ")
    List<Message> findConversation(User user1, User user2, Sort sort);
}
