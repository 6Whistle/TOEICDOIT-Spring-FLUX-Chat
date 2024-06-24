package site.toeicdoit.chat.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.domain.model.RoleModel;
import site.toeicdoit.chat.domain.model.RoomModel;
import site.toeicdoit.chat.domain.model.UserModel;

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
                UserModel.builder()
                .email("admin1@admin")
                .password(passwordEncoder.encode("admin"))
                .firstName("Junhwei")
                .lastName("Lee")
                .profile("test url1")
                .roles(List.of(RoleModel.SUPER_ADMIN, RoleModel.ADMIN, RoleModel.USER))
                .build(),
                UserModel.builder()
                .email("admin2@admin")
                .password(passwordEncoder.encode("admin"))
                .firstName("Junhwei")
                .lastName("Lee")
                .profile("test url2")
                .roles(List.of(RoleModel.SUPER_ADMIN, RoleModel.ADMIN, RoleModel.USER))
                .build()
            )
        ))
        .collectList()
        .flatMap(i -> mongoTemplate.insert(RoomModel.builder()
            .title("test room")
            .members(List.of(i.get(0).getId(), i.get(1).getId()))
            .build()
        ))
        .subscribe();

        return args -> {
            System.out.println("MongoDB Initiated!");
        };
    }
}