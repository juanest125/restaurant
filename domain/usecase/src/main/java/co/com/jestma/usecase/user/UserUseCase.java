package co.com.jestma.usecase.user;

import co.com.jestma.model.response.Response;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import co.com.jestma.model.user.User;
import co.com.jestma.model.user.gateways.UserRepository;
import co.com.jestma.usecase.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final ResponseUtils<User> userResponseUtils;
    private final ResponseUtils<List<User>> usersResponseUtils;

    private static final String EMAIL_REGEX = "^(.+)@(.+)$";
    private static final String PASS_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#?\\]]).{10,}$";

    public Mono<Response<User>> signup(User userIn) {
        return Mono.just(userIn)
                .flatMap(this::validateUserEmail)
                .flatMap(this::validateUserPassword)
                .flatMap(this::validateUserEmailDuplicated)
                .map(this::completeUser)
                .flatMap(userRepository::saveData)
                .flatMap(userResponseUtils::getResponseType)
                .onErrorResume(throwable -> userResponseUtils.getResponseTypeError(userIn, throwable))
                ;
    }

    private Mono<User> validateUserEmailDuplicated(User user) {
        return userRepository.findByEmail(user.getEmail())
                .hasElement()
                .filter(aBoolean -> !aBoolean)
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "The email is already used")))
                .thenReturn(user);
    }

    private User completeUser(User user) {
        return user.toBuilder()
                .id(UUID.randomUUID().toString())
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }

    public Mono<Response<List<User>>> findAll() {
        return userRepository.getAll()
                .map(user -> User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .lastName(user.getLastName())
                        .created(user.getCreated())
                        .updated(user.getUpdated())
                        .build()
                )
                .collectList()
                .flatMap(usersResponseUtils::getResponseType)
                .onErrorResume(throwable -> usersResponseUtils.getResponseTypeError(List.of(), throwable))
                ;

    }

    private Mono<User> validateUserPassword(User userIn) {
        return Mono.just(userIn)
                .filter(user -> !isNull(user.getPassword()))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "User password can not be null")))
                .filter(user -> user.getPassword().matches(PASS_REGEX))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "User password does not have a valid format: Minimum 10 chars, one lowercase letter, one uppercase letter and one special char ! @ # ? ]")));
    }

    private Mono<User> validateUserEmail(User userIn) {
        return Mono.just(userIn)
                .filter(user -> !isNull(user.getEmail()))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "User email can not be null")))
                .filter(user -> user.getEmail().matches(EMAIL_REGEX))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("401", "User email does not have a valid format")));
    }

}
