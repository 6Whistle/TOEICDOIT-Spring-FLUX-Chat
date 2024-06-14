package site.toeicdoit.chat.user.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import site.toeicdoit.chat.user.domain.model.RoleModel;

@Data
@Builder
public class UserDTO {
    private String id;
    private String email;
    private String password;
    private String profile;
    private String firstName;
    private String lastName;
    private List<RoleModel> roles;
}
