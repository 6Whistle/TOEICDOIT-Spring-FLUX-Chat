package site.toeicdoit.chat.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.common.domain.dto.Messenger;
import site.toeicdoit.chat.security.domain.LoginDTO;
import site.toeicdoit.chat.security.service.SecurityService;
import site.toeicdoit.chat.user.domain.dto.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security")
public class SecurityController {
    private final SecurityService securityService;
    
    @PostMapping("/login")
    public Mono<Messenger> login(@RequestBody LoginDTO entity) {
        return securityService.login(entity);
    }
    
    @PostMapping("/save")
    public Mono<Messenger> save(@RequestBody UserDTO entity) {
        return securityService.register(entity);
    }
}
