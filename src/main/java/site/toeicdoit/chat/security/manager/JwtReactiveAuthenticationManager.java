package site.toeicdoit.chat.security.manager;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.security.service.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
        .log();
        // .filter(i -> jwtTokenProvider.validateAccessToken(i.getCredentials().toString()))
        // .flatMap(i -> Mono.just(jwtTokenProvider.getAuthentication(i.getCredentials().toString())))
        // .onErrorMap(i -> new Exception("Invalid Token"));
    }
    
}
