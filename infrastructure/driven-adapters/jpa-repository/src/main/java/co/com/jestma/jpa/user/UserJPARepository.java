package co.com.jestma.jpa.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserJPARepository extends CrudRepository<UserEntity, String>, QueryByExampleExecutor<UserEntity> {
    UserEntity findByEmail(String email);
}
