package ru.mirea.server_coursework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.mirea.server_coursework.dto.MessageDTO;
import ru.mirea.server_coursework.model.Message;

import java.util.Collection;
import java.util.List;

/**
 * Описание класса
 */

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", source = "message.sender.username")
    @Mapping(target = "receiver", source = "message.receiver.username")
    MessageDTO toMessageDto(Message message);

    @Mapping(target = "sender", source = "message.sender.username")
    @Mapping(target = "receiver", source = "message.receiver.username")
    List<MessageDTO> toMessageDto(Collection<Message> messages);
}
