package site.toeicdoit.chat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.model.UserModel;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserModel, String>{
    Mono<UserModel> findByEmail(String email);
}
