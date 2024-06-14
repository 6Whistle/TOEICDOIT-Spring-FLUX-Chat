package site.toeicdoit.chat.user.domain.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
@Document(collection = "users")
public class UserModel{
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;

    private String password;

    private String profile;

    private String firstName;
    private String lastName;

    private List<RoleModel> roles;
    

}
