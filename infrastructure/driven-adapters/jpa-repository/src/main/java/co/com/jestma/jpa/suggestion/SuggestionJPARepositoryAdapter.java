package co.com.jestma.jpa.suggestion;

import co.com.jestma.jpa.helper.AdapterOperations;
import co.com.jestma.jpa.user.UserEntity;
import co.com.jestma.jpa.user.UserJPARepository;
import co.com.jestma.model.suggestion.Suggestion;
import co.com.jestma.model.suggestion.gateways.SuggestionRepository;
import co.com.jestma.model.user.User;
import co.com.jestma.model.user.gateways.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Repository
public class SuggestionJPARepositoryAdapter extends AdapterOperations<Suggestion, SuggestionEntity, String, SuggestionJPARepository>
 implements SuggestionRepository {
    public SuggestionJPARepositoryAdapter(SuggestionJPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Suggestion.class));
    }

    @Override
    public Mono<Suggestion> saveData(Suggestion suggestion){
        return Mono.just(suggestion)
                .map(super::toData)
                .map(repository::save)
                .map(super::toEntity)
                .doOnNext(suggestion1-> log.info("Suggestion created/updated {}", suggestion1))
                ;
    }

    @Override
    public Mono<Suggestion> deleteDataById(Suggestion suggestion) {
        repository.deleteById(suggestion.getId());
        return Mono.justOrEmpty(suggestion)
                .doOnNext(suggestion1-> log.info("Suggestion deleted {}", suggestion1));
    }

    @Override
    public Flux<Suggestion> findByUserId(String userId){
        return Flux.fromIterable(repository.findByUserId(userId).orElse(List.of()))
                .map(super::toEntity)
                ;

    }

    @Override
    public Flux<Suggestion> findByIsPrivate(Boolean isPrivate) {
        return Flux.fromIterable(repository.findByIsPrivate(isPrivate).orElse(List.of()))
                .map(super::toEntity)
                ;
    }

    @Override
    public Flux<Suggestion> findByUserIdOrIsPrivate(String userId, Boolean isPrivate) {
        return Flux.fromIterable(repository.findByUserIdOrIsPrivate(userId, isPrivate).orElse(List.of()))
                .map(super::toEntity)
                ;
    }

    @Override
    public Mono<Suggestion> getById(String id) {
        return Mono.justOrEmpty(repository.findById(id))
                .map(super::toEntity)
                ;
    }
}
