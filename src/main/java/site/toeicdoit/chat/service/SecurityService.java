package site.toeicdoit.chat.service;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.LoginDTO;
import site.toeicdoit.chat.domain.dto.Messenger;
import site.toeicdoit.chat.domain.dto.UserDTO;

public interface SecurityService {
    Mono<Messenger> login(LoginDTO entity);
    Mono<Messenger> register(UserDTO entity);
}
