package co.com.jestma.jpa.user;

import co.com.jestma.jpa.helper.AdapterOperations;
import co.com.jestma.model.user.User;
import co.com.jestma.model.user.gateways.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Repository
public class UserJPARepositoryAdapter extends AdapterOperations<User, UserEntity, String, UserJPARepository>
 implements UserRepository {
    public UserJPARepositoryAdapter(UserJPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class/* change for domain model */));
    }

    @Override
    public Mono<User> saveData(User user){
        return Mono.just(user)
                .map(super::toData)
                .map(userEntity -> repository.save(userEntity))
                .map(super::toEntity)
                .map(user1 -> user1.toBuilder().password("").build())
                .doOnNext(user1 -> log.info("User created {}", user1))
                ;
    }

    @Override
    public Flux<User> getAll() {
        return Flux.fromIterable(super.findAll());
    }

    @Override
    public Mono<User> findByEmail(String email){
        return Mono.justOrEmpty(repository.findByEmail(email))
                .map(super::toEntity)
                ;

    }
}
