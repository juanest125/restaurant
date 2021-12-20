package co.com.jestma.jpa.suggestion;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface SuggestionJPARepository extends CrudRepository<SuggestionEntity, String>, QueryByExampleExecutor<SuggestionEntity> {
    Optional<List<SuggestionEntity>> findByUserId(String userId);
    Optional<List<SuggestionEntity>> findByIsPrivate(Boolean isPrivate);
    Optional<List<SuggestionEntity>> findByUserIdOrIsPrivate(String userId, Boolean isPrivate);
}
