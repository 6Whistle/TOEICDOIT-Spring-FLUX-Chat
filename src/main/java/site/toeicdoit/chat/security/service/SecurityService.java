package site.toeicdoit.chat.security.service;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.common.domain.dto.Messenger;
import site.toeicdoit.chat.security.domain.LoginDTO;
import site.toeicdoit.chat.user.domain.dto.UserDTO;

public interface SecurityService {
    Mono<Messenger> login(LoginDTO entity);
    Mono<Messenger> register(UserDTO entity);
}
