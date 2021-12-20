package co.com.jestma.usecase.randomnumber;

import co.com.jestma.model.randomnumber.RandomNumber;
import co.com.jestma.model.randomnumber.gateways.RandomNumberRepository;
import co.com.jestma.model.response.Response;
import co.com.jestma.usecase.utility.ResponseUtils;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RandomNumberUseCase {
    private final RandomNumberRepository repository;
    private final ResponseUtils<RandomNumber> randomNumberResponseUtils;

    public Mono<Response<RandomNumber>> getRandomNumber(Integer min, Integer max, Integer count){
        return repository.getRandom(min, max, count)
                .flatMap(randomNumberResponseUtils::getResponseType)
                .onErrorResume(throwable -> randomNumberResponseUtils.getResponseTypeError(RandomNumber.builder().build(), throwable))
                ;
    }
}
