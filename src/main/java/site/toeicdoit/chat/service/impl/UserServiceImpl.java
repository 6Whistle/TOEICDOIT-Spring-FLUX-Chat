package site.toeicdoit.chat.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.domain.dto.UserDTO;
import site.toeicdoit.chat.domain.model.UserFluxModel;
import site.toeicdoit.chat.repository.UserRepository;
import site.toeicdoit.chat.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public Mono<UserFluxModel> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<UserFluxModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Flux<UserFluxModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserFluxModel> save(UserDTO dto) {
        return userRepository.findByEmail(dto.getEmail())
            .flatMap(user -> Mono.just(new UserFluxModel()))
            .switchIfEmpty(userRepository.save(UserFluxModel.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .profile(dto.getProfile())
                .name(dto.getName())
                .roles(dto.getRoles())
                .build()));
    }

    @Override
    public Mono<UserFluxModel> update(UserDTO dto) {
        return userRepository.findById(dto.getId())
            .flatMap(user -> {
                user.setEmail(dto.getEmail());
                // user.setPassword(dto.getPassword());
                user.setProfile(dto.getProfile());
                user.setName(dto.getName());
                user.setRoles(dto.getRoles());
                return Mono.just(user);
            })
            .flatMap(userRepository::save)
            .switchIfEmpty(Mono.just(new UserFluxModel()));
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return userRepository.findById(id)
            .flatMap(user -> userRepository.delete(user).then(Mono.just(Boolean.TRUE)))
            .switchIfEmpty(Mono.just(Boolean.FALSE));
    }

    @Override
    public Mono<Long> count() {
        return userRepository.count();
    }

}
