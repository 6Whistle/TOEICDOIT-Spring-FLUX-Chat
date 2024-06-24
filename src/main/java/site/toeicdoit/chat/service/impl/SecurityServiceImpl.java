package site.toeicdoit.chat.service.impl;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.LoginDTO;
import site.toeicdoit.chat.domain.dto.Messenger;
import site.toeicdoit.chat.domain.dto.UserDTO;
import site.toeicdoit.chat.domain.model.RoleModel;
import site.toeicdoit.chat.domain.model.UserModel;
import site.toeicdoit.chat.repository.UserRepository;
import site.toeicdoit.chat.service.SecurityService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Messenger> login(LoginDTO entity) {
        
        log.info("LoginDTO: {}", entity);
        return userRepository.findByEmail(entity.getEmail())
        .log()
        .filter(i -> passwordEncoder.matches(entity.getPassword(), i.getPassword()))
        .log()
        .doOnNext(i -> i.setPassword(""))
        .map(i -> Messenger.builder()
            .message("SUCCESS")
            .data(i)
            .accessToken(jwtTokenProvider.generateToken((UserDetails)i, false))
            .refreshToken(jwtTokenProvider.generateToken((UserDetails)i, true))
            .accessTokenExpired(jwtTokenProvider.getAccessTokenExpired())
            .refreshTokenExpired(jwtTokenProvider.getRefreshTokenExpired())
            .build());
    }

    @Override
    public Mono<Messenger> register(UserDTO entity) {
        return userRepository.findByEmail(entity.getEmail())
        .hasElement()
        .filter(i -> !i && entity.getPassword() != null && entity.getPassword().length() > 0)
        .flatMap(i -> userRepository.save(UserModel.builder()
            .email(entity.getEmail())
            .password(passwordEncoder.encode(entity.getPassword()))
            .profile("")
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .roles(List.of(RoleModel.USER))
            .build()))
        .map(i -> Messenger.builder()
            .message("SUCCESS")
            .data(i)
            .build());
    }
 
    
}
