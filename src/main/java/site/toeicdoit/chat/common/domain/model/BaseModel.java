package site.toeicdoit.chat.common.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;


@Getter
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
