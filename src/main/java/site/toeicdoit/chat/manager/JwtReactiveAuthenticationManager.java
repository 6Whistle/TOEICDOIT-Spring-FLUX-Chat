package site.toeicdoit.chat.manager;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.model.JwtToken;
import site.toeicdoit.chat.exception.JwtAuthenticationException;
import site.toeicdoit.chat.service.impl.JwtTokenProvider;

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
