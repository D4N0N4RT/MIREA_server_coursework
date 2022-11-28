package ru.mirea.server_coursework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.mirea.server_coursework.dto.MessageDTO;
import ru.mirea.server_coursework.exception.WrongRSQLQueryException;
import ru.mirea.server_coursework.mapper.MessageMapper;
import ru.mirea.server_coursework.model.Message;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.repository.MessageRepository;
import ru.mirea.server_coursework.service.MessageService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

/**
 * Описание класса
 */
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    private MessageService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new MessageService(messageRepository, messageMapper);
    }

    @Test
    public void shouldCreate() {
        //Arrange
        Message message = new Message();
        message.setContent("Test");
        User user1 = User.builder().username("user").build();
        User user2 = User.builder().username("user2").build();
        message.setReceiver(user2);
        message.setSender(user1);
        //Act
        underTest.create(message);
        ArgumentCaptor<Message> captor =
                ArgumentCaptor.forClass(Message.class);
        //Assert
        Mockito.verify(messageRepository).create(captor.capture());
        Message captured = captor.getValue();
        Assertions.assertEquals(captured, message);
    }

    @Test
    public void shouldFindConversation() throws WrongRSQLQueryException {
        //Arrange
        Message message = Message.builder().content("Test")
                .time(LocalDateTime.now()).build();
        User user1 = new User();
        user1.setUsername("user");
        User user2 = new User();
        user2.setUsername("user2");
        Mockito.when(messageRepository.findConversation(user1,user2, Sort.by("time"))).thenReturn(new ArrayList<>(List.of(message)));
        Mockito.when(messageMapper.toMessageDto(any(ArrayList.class)))
                .thenReturn(new ArrayList<>(List.of(
                        MessageDTO.builder()
                                .content("Test")
                                .time(LocalDateTime.now())
                                .build()
                )));
        List<MessageDTO> check = underTest.getConversation(user1,user2);
        //Assert
        Mockito.verify(messageRepository).findConversation(user1,user2, Sort.by("time"));
        Assertions.assertNotNull(check);
        Assertions.assertEquals(1, check.size());
    }
}
