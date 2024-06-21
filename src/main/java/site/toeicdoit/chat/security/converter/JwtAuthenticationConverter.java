package site.toeicdoit.chat.security.converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.security.domain.BearerToken;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter{

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest())
        .flatMap(i -> Mono.just(i.getHeaders()))
        .filter(i -> i.containsKey("Authorization"))
        .filter(i -> i.getFirst("Authorization").startsWith("Bearer "))
        .flatMap(i -> Mono.just(i.getFirst("Authorization").substring(7)))
        .flatMap(i -> Mono.just(new BearerToken(i)));
    }
    
}
