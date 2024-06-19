package site.toeicdoit.chat.room.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.room.domain.dto.ChatDTO;
import site.toeicdoit.chat.room.domain.dto.RoomDTO;
import site.toeicdoit.chat.room.domain.exception.ChatException;
import site.toeicdoit.chat.room.domain.model.ChatModel;
import site.toeicdoit.chat.room.domain.model.RoomModel;
import site.toeicdoit.chat.room.service.RoomService;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/checkServer")
    public Mono<String> getMethodName() {
        log.info("Check server status");
        return roomService.countConnection()
            .flatMap(count -> Mono.just("Server is running. Total connection: " + count));
    }

    @PostMapping("/save")
    public Mono<RoomModel> saveRoom(@RequestBody RoomDTO dto) {
        log.info("Save room");
        return roomService.save(dto);
    }
    

    @GetMapping("/recieve/{roomId}")
    public Flux<ServerSentEvent<ChatModel>> subscribeByRoomId(@PathVariable String roomId) {
        log.info("subscribe chat by room id {}", roomId);
        return roomService.subscribeByRoomId(roomId)
        .switchIfEmpty(Flux.error(new ChatException("Room not found")));
    }

    @PostMapping("/send")
    public Mono<ChatModel> sendChat(@RequestBody ChatDTO chatDTO) {
        log.info("Send chat {}", chatDTO.toString());
        return roomService.saveChat(chatDTO)
        .switchIfEmpty(Mono.error(new ChatException("Room not found")));
    }
       
}
