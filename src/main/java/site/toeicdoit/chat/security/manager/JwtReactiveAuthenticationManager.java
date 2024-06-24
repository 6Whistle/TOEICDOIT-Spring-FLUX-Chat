package site.toeicdoit.chat.security.manager;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.security.domain.JwtToken;
import site.toeicdoit.chat.security.domain.exception.JwtAuthenticationException;
import site.toeicdoit.chat.security.service.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
        .cast(JwtToken.class)
        .filter(i -> jwtTokenProvider.isTokenValid(i.getToken()))
        .flatMap(i -> Mono.just(i.withAuthentication(true)))
        .switchIfEmpty(Mono.error(new JwtAuthenticationException("Invalid Token")));
    }
    
}
