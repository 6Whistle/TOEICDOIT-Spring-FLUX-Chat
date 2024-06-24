package site.toeicdoit.chat.service;

import org.springframework.http.codec.ServerSentEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.ChatDTO;
import site.toeicdoit.chat.domain.dto.RoomDTO;
import site.toeicdoit.chat.domain.model.ChatModel;
import site.toeicdoit.chat.domain.model.RoomModel;

public interface RoomService extends CommandService<RoomModel, RoomDTO>, QueryService<RoomModel, RoomDTO>{
    Mono<ChatDTO> saveChat(ChatDTO chatDTO);
    Mono<ChatModel> findChatById(String id);
    Flux<ChatModel> findChatsByRoomId(String roomId);
    Flux<ServerSentEvent<ChatDTO>> subscribeByRoomId(String roomId);
    Mono<Integer> countConnection();
}
