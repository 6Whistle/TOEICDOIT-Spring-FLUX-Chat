package site.toeicdoit.chat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.LoginDTO;
import site.toeicdoit.chat.domain.dto.Messenger;
import site.toeicdoit.chat.domain.dto.UserDTO;
import site.toeicdoit.chat.service.SecurityService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security")
public class SecurityController {
    private final SecurityService securityService;
    
    @PostMapping("/login")
    public Mono<Messenger> login(@RequestBody LoginDTO entity) {
        log.info("LoginDTO: {}", entity);
        return securityService.login(entity);
    }
    
    @PostMapping("/save")
    public Mono<Messenger> save(@RequestBody UserDTO entity) {
        return securityService.register(entity);
    }
}
