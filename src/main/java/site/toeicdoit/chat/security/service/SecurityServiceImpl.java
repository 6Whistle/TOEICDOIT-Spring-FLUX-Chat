package site.toeicdoit.chat.security.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.common.domain.dto.Messenger;
import site.toeicdoit.chat.security.domain.LoginDTO;
import site.toeicdoit.chat.user.domain.dto.UserDTO;
import site.toeicdoit.chat.user.domain.model.RoleModel;
import site.toeicdoit.chat.user.domain.model.UserModel;
import site.toeicdoit.chat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Messenger> login(LoginDTO entity) {
        return userRepository.findByEmail(entity.getEmail())
        .filter(i -> passwordEncoder.matches(entity.getPassword(), i.getPassword()))
        .doOnNext(i -> i.setPassword(""))
        .map(i -> Messenger.builder()
            .message("SUCCESS")
            .data(i)
            .accessToken(jwtTokenProvider.generateToken(i, false))
            .refreshToken(jwtTokenProvider.generateToken(i, true))
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
