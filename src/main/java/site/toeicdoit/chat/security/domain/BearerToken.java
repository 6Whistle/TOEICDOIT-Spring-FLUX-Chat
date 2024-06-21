package site.toeicdoit.chat.security.domain;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class BearerToken extends AbstractAuthenticationToken{
    private String token;

    public BearerToken(){
        super(AuthorityUtils.NO_AUTHORITIES);
    }

    // public BearerToken(Collection<? extends GrantedAuthority> authorities) {
    //     super(authorities);
    // }

    public BearerToken(String token) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
    
}
