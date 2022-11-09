package ru.mirea.server_coursework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mirea.server_coursework.dto.MessageDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @SequenceGenerator(name = "messagesIdSeq",
            sequenceName = "messages_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messagesIdSeq")
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    private User receiver;

    private String content;

    private LocalDateTime time;

    public MessageDTO toDTO() {
        return MessageDTO.builder().sender(this.sender.getName() + ' ' + this.sender.getSurname()
                        + " (" + this.sender.getUsername() + ')').receiver(this.receiver.getName()
                        + ' ' + this.receiver.getSurname() + " (" + this.receiver.getUsername() + ')')
                .content(this.content).time(this.time).build();
    }
}
