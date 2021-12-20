package co.com.jestma.model.user.gateways;

import co.com.jestma.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveData(User user);
    Mono<User> findByEmail(String email);
    Flux<User> getAll();
}
