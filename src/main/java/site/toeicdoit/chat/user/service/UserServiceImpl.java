package site.toeicdoit.chat.user.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.toeicdoit.chat.user.domain.dto.UserDTO;
import site.toeicdoit.chat.user.domain.model.UserModel;
import site.toeicdoit.chat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public Mono<UserModel> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Flux<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<UserModel> save(UserDTO dto) {
        return userRepository.findByEmail(dto.getEmail())
            .flatMap(user -> Mono.just(new UserModel()))
            .switchIfEmpty(userRepository.save(UserModel.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .profile(dto.getProfile())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roles(dto.getRoles())
                .build()));
    }

    @Override
    public Mono<UserModel> update(UserDTO dto) {
        return userRepository.findById(dto.getId())
            .flatMap(user -> {
                user.setEmail(dto.getEmail());
                user.setPassword(dto.getPassword());
                user.setProfile(dto.getProfile());
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setRoles(dto.getRoles());
                return Mono.just(user);
            })
            .flatMap(userRepository::save)
            .switchIfEmpty(Mono.just(new UserModel()));
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