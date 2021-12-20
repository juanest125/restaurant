package co.com.jestma.usecase.user;

import co.com.jestma.model.credential.Credential;
import co.com.jestma.model.credential.gateways.CredentialRepository;
import co.com.jestma.model.response.Response;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import co.com.jestma.model.user.User;
import co.com.jestma.model.user.gateways.UserRepository;
import co.com.jestma.model.usersession.UserSession;
import co.com.jestma.model.usersession.gateways.UserSessionToken;
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
    private final ResponseUtils<UserSession> credentialResponseUtils;
    private final UserSessionToken token;
    private final CredentialRepository credentialRepository;

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

    public Mono<Response<UserSession>> login(Credential credential) {
        return userRepository.findByEmail(credential.getEmail())
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "Email is not registered")))
                .flatMap(this::validateUserEmail)
                .flatMap(user -> Mono.just(User.builder().password(credential.getPassword()).build())
                        .flatMap(this::validateUserPassword)
                        .thenReturn(user)
                )
                .flatMap(user -> Mono.just(credential.getPassword())
                        .filter(password ->credentialRepository.validatePasswords(password, user.getPassword()))
                        .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "Invalid credentials")))
                        .map(password -> UserSession.builder()
                                .id(UUID.randomUUID().toString())
                                .userId(user.getId())
                                .token(token.getToken(user.getEmail()))
                                .created(LocalDateTime.now())
                                .build()
                        )
                )
                .flatMap(credentialResponseUtils::getResponseType)
                .onErrorResume(throwable -> credentialResponseUtils.getResponseTypeError(UserSession.builder().build(), throwable))
                ;
    }

    private Mono<User> validateUserEmailDuplicated(User user) {
        return userRepository.findByEmail(user.getEmail())
                .hasElement()
                .filter(aBoolean -> !aBoolean)
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "The email is already used")))
                .thenReturn(user);
    }

    private User completeUser(User user) {
        return user.toBuilder()
                .id(UUID.randomUUID().toString())
                .password(credentialRepository.getPasswordEncoded(user.getPassword()))
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
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "User password can not be null")))
                .filter(user -> user.getPassword().matches(PASS_REGEX))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "User password does not have a valid format: Minimum 10 chars, one lowercase letter, one uppercase letter and one special char ! @ # ? ]")));
    }

    private Mono<User> validateUserEmail(User userIn) {
        return Mono.just(userIn)
                .filter(user -> !isNull(user.getEmail()))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "User email can not be null")))
                .filter(user -> user.getEmail().matches(EMAIL_REGEX))
                .switchIfEmpty(Mono.error(new RestaurantThrowable("400", "User email does not have a valid format")));
    }

}
