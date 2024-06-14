package site.toeicdoit.chat.user.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.user.domain.model.UserModel;

public interface UserRepository extends ReactiveMongoRepository<UserModel, String>{

    Mono<UserModel> findByEmail(String email);
    
}
