package co.com.jestma.model.randomnumber.gateways;

import co.com.jestma.model.randomnumber.RandomNumber;
import reactor.core.publisher.Mono;

public interface RandomNumberRepository {
    Mono<RandomNumber> getRandom(Integer min, Integer max, Integer count);
}
