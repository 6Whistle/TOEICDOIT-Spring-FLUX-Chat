package site.toeicdoit.chat.room.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatModel {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
}
