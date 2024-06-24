package site.toeicdoit.chat.security.converter;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.security.domain.JwtToken;
import site.toeicdoit.chat.security.service.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements ServerAuthenticationConverter{

    private final JwtTokenProvider jwtTokenProvider;
    private static final String BEARER = "Bearer ";

    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
        .filter(i -> i.startsWith(BEARER))
        .flatMap(i -> Mono.just(i.substring(BEARER.length())))
        .flatMap(i -> Mono.just(new JwtToken(i, createUserDetails(i))));
    }

    private UserDetails createUserDetails(String token){
        String email = jwtTokenProvider.extractEmail(token);
        return User.builder()
        .username(email)
        .authorities(createAuthorities(token))
        .password("")
        .build();
    }

    private List<SimpleGrantedAuthority> createAuthorities(String token){
        return jwtTokenProvider.extractRoles(token).stream()
        .map(role -> "ROLE_" + role)
        .map(SimpleGrantedAuthority::new)
        .toList();
    }

    // @Override
    // public Mono<Authentication> convert(ServerWebExchange exchange) {
    //     return Mono.justOrEmpty(exchange.getRequest())
    //     .flatMap(i -> Mono.just(i.getHeaders()))
    //     .filter(i -> i.containsKey("Authorization"))
    //     .filter(i -> i.getFirst("Authorization").startsWith("Bearer "))
    //     .flatMap(i -> Mono.just(i.getFirst("Authorization").substring(7)))
    //     .flatMap(i -> Mono.just(new JwtToken(i)));
    // }
    
}
