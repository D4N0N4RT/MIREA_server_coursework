package ru.mirea.server_coursework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String sender;
    private String receiver;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime time;
}
