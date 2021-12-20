package co.com.jestma.model.suggestion.gateways;

import co.com.jestma.model.suggestion.Suggestion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SuggestionRepository {
    Mono<Suggestion> saveData(Suggestion suggestion);
    Mono<Suggestion> deleteDataById(Suggestion suggestion);
    Mono<Suggestion> getById(String id);
    Flux<Suggestion> findByUserId(String userId);
    Flux<Suggestion> findByIsPrivate(Boolean isPrivate);
    Flux<Suggestion> findByUserIdOrIsPrivate(String userId, Boolean isPrivate);
}
