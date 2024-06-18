package site.toeicdoit.chat.room.domain.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "rooms")
public class RoomModel {
    @Id
    private String id;
    private String title;
    private List<String> members;
}
