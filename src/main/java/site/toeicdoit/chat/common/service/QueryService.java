package site.toeicdoit.chat.common.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryService<Model, DTO> {
    Mono<Model> findById(String id);
    Mono<Model> findByEmail(String email);
    Flux<Model> findAll();
    Mono<Long> count();
}
