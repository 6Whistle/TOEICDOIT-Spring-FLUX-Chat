package site.toeicdoit.chat.room.service;

import org.springframework.http.codec.ServerSentEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.common.service.CommandService;
import site.toeicdoit.chat.common.service.QueryService;
import site.toeicdoit.chat.room.domain.dto.ChatDTO;
import site.toeicdoit.chat.room.domain.dto.RoomDTO;
import site.toeicdoit.chat.room.domain.model.ChatModel;
import site.toeicdoit.chat.room.domain.model.RoomModel;

public interface RoomService extends CommandService<RoomModel, RoomDTO>, QueryService<RoomModel, RoomDTO>{
    Mono<ChatModel> saveChat(ChatDTO chatDTO);
    Mono<ChatModel> findChatById(String id);
    Flux<ChatModel> findChatsByRoomId(String roomId);
    Flux<ServerSentEvent<ChatModel>> subscribeByRoomId(String roomId);
    Mono<Integer> countConnection();
}
