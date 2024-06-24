package site.toeicdoit.chat.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import site.toeicdoit.chat.domain.model.RoleModel;

@Data
@Builder
public class UserDTO {
    private String id;
    private String email;
    private String password;
    private String profile;
    private String name;
    private List<RoleModel> roles;
}
