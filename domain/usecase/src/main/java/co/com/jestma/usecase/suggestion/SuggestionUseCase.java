package co.com.jestma.usecase.suggestion;

import co.com.jestma.model.response.Response;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import co.com.jestma.model.suggestion.Suggestion;
import co.com.jestma.model.suggestion.gateways.SuggestionRepository;
import co.com.jestma.model.user.User;
import co.com.jestma.usecase.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class SuggestionUseCase {
    private final SuggestionRepository repository;
    private final ResponseUtils<Suggestion> suggestionResponseUtils;
    private final ResponseUtils<List<Suggestion>> suggestionsResponseUtils;

    public Mono<Response<Suggestion>> create (Suggestion suggestionIn, User user){
        return Mono.just(suggestionIn)
                .map(suggestion -> suggestion.toBuilder()
                        .id(UUID.randomUUID().toString())
                        .userId(user.getId())
                        .created(LocalDateTime.now())
                        .updated(LocalDateTime.now())
                        .build()
                )
                .flatMap(repository::saveData)
                .flatMap(suggestionResponseUtils::getResponseType)
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(suggestionIn, throwable))
                ;
    }

    public Mono<Response<Suggestion>> update (Suggestion suggestionIn, User user){
        return repository.getById(suggestionIn.getId())
                .filter(suggestion -> suggestion.getUserId().equals(user.getId()))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "Unauthorized to update")))
                .map(suggestion -> suggestion.toBuilder()
                        .comment(suggestionIn.getComment())
                        .isPrivate(suggestionIn.getIsPrivate())
                        .updated(LocalDateTime.now())
                        .build()
                )
                .flatMap(repository::saveData)
                .flatMap(suggestionResponseUtils::getResponseType)
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(suggestionIn, throwable))
                ;
    }

    public Mono<Response<List<Suggestion>>> getAll(Integer page, Integer size, User user){
        return repository.findByUserIdOrIsPrivate(page, size, user.getId(), false)
                .switchIfEmpty(Mono.empty())
                .collectList()
                .flatMap(suggestionsResponseUtils::getResponseType)
                .onErrorResume(throwable -> suggestionsResponseUtils.getResponseTypeError(List.of(), throwable))
                ;
    }

    public Mono<Response<Suggestion>> delete (String id, User user){
        return repository.getById(id)
                .filter(suggestion -> suggestion.getUserId().equals(user.getId()))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "Unauthorized to delete")))
                .flatMap(repository::deleteDataById)
                .flatMap(suggestionResponseUtils::getResponseType)
                .onErrorResume(throwable -> suggestionResponseUtils.getResponseTypeError(Suggestion.builder().build(), throwable))
                ;
    }
}
