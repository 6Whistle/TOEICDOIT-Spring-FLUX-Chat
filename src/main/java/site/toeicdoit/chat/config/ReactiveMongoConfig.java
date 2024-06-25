package site.toeicdoit.chat.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.domain.model.Role;
import site.toeicdoit.chat.domain.model.RoomFluxModel;
import site.toeicdoit.chat.domain.model.UserFluxModel;

@Configuration
@RequiredArgsConstructor
public class ReactiveMongoConfig {
    private final ReactiveMongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() {
        mongoTemplate.getCollectionNames()
        .flatMap(collectionName -> mongoTemplate.dropCollection(collectionName))
        .log()
        .collectList()
        .flatMapMany(i -> mongoTemplate.insertAll(
            List.of(
                UserFluxModel.builder()
                .email("admin1@admin")
                .password(passwordEncoder.encode("admin"))
                .name("Junhwei")
                .profile("test url1")
                .roles(List.of(Role.SUPER_ADMIN, Role.ADMIN, Role.USER))
                .build(),
                UserFluxModel.builder()
                .email("admin2@admin")
                .password(passwordEncoder.encode("admin"))
                .name("Junhwei")
                .profile("test url2")
                .roles(List.of(Role.SUPER_ADMIN, Role.ADMIN, Role.USER))
                .build()
            )
        ))
        .collectList()
        .flatMap(i -> mongoTemplate.insert(RoomFluxModel.builder()
            .title("test room")
            .members(List.of(i.get(0).getId()))
            .members(List.of(i.get(0).getId(), i.get(1).getId()))
            .build()
        ))
        .subscribe();

        return args -> {
            System.out.println("MongoDB Initiated!");
        };
    }
}