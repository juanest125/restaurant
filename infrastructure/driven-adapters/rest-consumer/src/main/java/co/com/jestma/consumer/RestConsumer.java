package co.com.jestma.consumer;

import co.com.jestma.model.randomnumber.RandomNumber;
import co.com.jestma.model.randomnumber.gateways.RandomNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestConsumer implements RandomNumberRepository
{

    private final WebClient client;


    public Mono<RandomNumber> getRandom(Integer min, Integer max, Integer count) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.queryParam("min", min)
                        .queryParam("max", max)
                        .queryParam("count", count)
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList()
                .map(numbers -> RandomNumber.builder()
                        .numbers(numbers)
                        .build()
                )
                .doOnNext(value -> log.info("::: RandomNumbers from public API -> {}", value))
                ;

    }
}