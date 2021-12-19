package co.com.jestma.usecase.utility;

import co.com.jestma.enums.Messages;
import co.com.jestma.model.response.Response;
import co.com.jestma.model.restaurantexception.RestaurantThrowable;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ResponseUtils<T> {

    public static ResponseUtils factory(){
        return new ResponseUtils();
    }

    public Mono<Response<T>> getResponseType(T t) {
        return Mono.just(Response.<T>builder()
                .code(201)
                .status(Messages.SUCCESS.getId())
                .body(t)
                .idTransaction(UUID.randomUUID().toString())
                .build()
        );
    }

    public Mono<Response<T>> getResponseTypeError(T t, Throwable throwable) {
        return Mono.just(throwable)
                .filter(RestaurantThrowable.class::isInstance)
                .map(throwable1 -> Response.<T>builder()
                        .code(Integer.parseInt(((RestaurantThrowable) throwable).getCode()))
                        .build()
                )
                .switchIfEmpty(Mono.just(Response.<T>builder()
                                .code(500)
                                .build()
                        )
                )
                .map(result -> result.toBuilder()
                        .status("ERROR")
                        .message(throwable.getMessage())
                        .body(t)
                        .idTransaction(UUID.randomUUID().toString())
                        .build()
                );
    }
}
