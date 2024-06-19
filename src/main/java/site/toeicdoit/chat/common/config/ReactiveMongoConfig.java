package site.toeicdoit.chat.common.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.room.domain.model.RoomModel;
import site.toeicdoit.chat.user.domain.model.RoleModel;
import site.toeicdoit.chat.user.domain.model.UserModel;

@Configuration
@RequiredArgsConstructor
public class ReactiveMongoConfig {
    private final ReactiveMongoTemplate mongoTemplate;

    @Bean
    public CommandLineRunner commandLineRunner() {
        mongoTemplate.getCollectionNames()
        .flatMap(collectionName -> mongoTemplate.dropCollection(collectionName))
        .log()
        .collectList()
        // .flatMap(i -> mongoTemplate.createCollection(UserModel.class, CollectionOptions.empty().capped().size(1024).maxDocuments(100)))
        // .flatMap(i -> mongoTemplate.createCollection(ChatModel.class, CollectionOptions.empty().capped().size(1024).maxDocuments(100)))
        .flatMap(i -> mongoTemplate.insert(UserModel.builder()
                .email("admin@admin")
                .password("admin")
                .firstName("Junhwei")
                .lastName("Lee")
                .profile("test url")
                .roles(List.of(RoleModel.SUPER_ADMIN, RoleModel.ADMIN, RoleModel.USER))
                .build()))
        .flatMap(i -> mongoTemplate.insert(RoomModel.builder()
            .title("test room")
            .members(List.of(i.getId()))
            .build()))
        .subscribe();

        return args -> {
            System.out.println("MongoDB Initiated!");
        };
    }
}