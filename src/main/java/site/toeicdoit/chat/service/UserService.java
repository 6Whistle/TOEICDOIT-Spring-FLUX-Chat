package site.toeicdoit.chat.service;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.UserDTO;
import site.toeicdoit.chat.domain.model.UserModel;

public interface UserService extends QueryService<UserModel, UserDTO>, CommandService<UserModel, UserDTO>{
    Mono<UserModel> findByEmail(String email);
}
