package site.toeicdoit.chat.domain.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;
import site.toeicdoit.chat.domain.model.RoleModel;

@Data
@Builder
public class UserDTO implements UserDetails {
    private String id;
    private String email;
    private String password;
    private String profile;
    private String firstName;
    private String lastName;
    private List<RoleModel> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> "ROLE_" + role.name()).map(SimpleGrantedAuthority::new).toList();
    }
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }
}
