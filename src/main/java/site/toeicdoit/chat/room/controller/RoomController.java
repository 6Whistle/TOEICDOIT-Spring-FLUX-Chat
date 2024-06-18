package site.toeicdoit.chat.room.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.room.service.RoomService;

import org.springframework.web.bind.annotation.GetMapping;


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
       
}
