package site.toeicdoit.chat.room.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import site.toeicdoit.chat.room.domain.dto.ChatDTO;
import site.toeicdoit.chat.room.domain.dto.RoomDTO;
import site.toeicdoit.chat.room.domain.exception.ChatException;
import site.toeicdoit.chat.room.domain.model.ChatModel;
import site.toeicdoit.chat.room.domain.model.RoomModel;
import site.toeicdoit.chat.room.repository.ChatRepository;
import site.toeicdoit.chat.room.repository.RoomRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final Map<String, Sinks.Many<ServerSentEvent<ChatModel>>> chatSinks;

    @PreDestroy
    public void close() {
        chatSinks.values().forEach(Sinks.Many::tryEmitComplete);
    }


    @Override
    public Mono<RoomModel> save(RoomDTO dto) {
        return roomRepository.save(RoomModel.builder()
            .title(dto.getTitle())
            .members(dto == null ? new ArrayList<>() : dto.getMembers())
            .build());
    }

    @Override
    public Mono<ChatModel> saveChat(ChatDTO chatDTO) {
        return roomRepository.findById(chatDTO.getRoomId())
        .filter(i -> i.getMembers().contains(chatDTO.getSender()))
        .flatMap(i -> chatRepository.save(ChatModel.builder()
            .roomId(chatDTO.getRoomId())
            .message(chatDTO.getMessage())
            .sender(chatDTO.getSender())
            .createdAt(LocalDateTime.now())
            .build()))
        .doOnSuccess(i -> {
            chatSinks.get(i.getRoomId()).tryEmitNext(ServerSentEvent.builder(i).build());
        });
    }

    @Override
    public Mono<RoomModel> update(RoomDTO dto) {
        return roomRepository.existsById(dto.getId())
            .flatMap(i -> roomRepository.save(RoomModel.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .members(dto.getMembers())
                .build()));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return roomRepository.existsById(id)
            .filter(i -> i)
            .flatMap(i -> roomRepository.deleteById(id).thenReturn(i));
    }

    @Override
    public Mono<RoomModel> findById(String id) {
        return roomRepository.findById(id);
    }

    @Override
    public Mono<ChatModel> findChatById(String id) {
        return chatRepository.findById(id);
    }

    @Override
    public Flux<ChatModel> findChatsByRoomId(String roomId) {
        return roomRepository.existsById(roomId)
            .filter(i -> i)
            .flatMapMany(i -> chatRepository.findByRoomId(roomId));
    }

    @Override
    public Flux<RoomModel> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Mono<Long> count() {
        return roomRepository.count();
    }
    
    @Override
    public Flux<ServerSentEvent<ChatModel>> subscribeByRoomId(String roomId) {
        return chatSinks.computeIfAbsent(roomId, i -> Sinks.many().replay().all())
            .asFlux()
            .doOnCancel(() -> {
                log.info("Unsubscribed room {}", roomId);
            })
            .doOnError((i) -> {
                log.error("Error in room {}", roomId, i);
                chatSinks.get(roomId).tryEmitError(new ChatException(i.getMessage()));
            })
            .doOnComplete(() -> {
                log.info("Complete room {}", roomId);
                chatSinks.get(roomId).tryEmitComplete();
                chatSinks.remove(roomId);
            });
    }

    @Override
    public Mono<Integer> countConnection() {
        return Mono.just(chatSinks.size());
    }
}
