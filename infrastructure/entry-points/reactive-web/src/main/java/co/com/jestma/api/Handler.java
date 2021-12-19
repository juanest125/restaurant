package co.com.jestma.api;

import co.com.jestma.api.dto.SignUpDto;
import co.com.jestma.model.user.User;
import co.com.jestma.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase useCase;
    private final ObjectMapper mapper;

    public Mono<ServerResponse> signUp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SignUpDto.class)
                .doOnNext(signUpDto -> log.info("::: Entering to signUp with requestBody {}", signUpDto))
                .map(signUpDto -> mapper.map(signUpDto, User.class))
                .flatMap(useCase::signup)
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value))
                ;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return useCase.findAll()
                .flatMap(value -> ServerResponse.status(value.getCode()).bodyValue(value));
    }
}
