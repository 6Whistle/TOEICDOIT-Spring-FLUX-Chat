package site.toeicdoit.chat.room.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import site.toeicdoit.chat.room.domain.model.RoomModel;

@Repository
public interface RoomRepository extends ReactiveMongoRepository<RoomModel, String>{
    
}
