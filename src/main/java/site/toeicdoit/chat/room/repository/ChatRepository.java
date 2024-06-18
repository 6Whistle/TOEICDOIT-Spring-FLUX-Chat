package site.toeicdoit.chat.room.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import site.toeicdoit.chat.room.domain.model.ChatModel;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<ChatModel, String>{
    Flux<ChatModel> findByRoomId(String roomId);
}