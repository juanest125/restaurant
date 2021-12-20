package co.com.jestma.jpa.suggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_suggestion")
public class SuggestionEntity {
    @Id
    private String id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "is_private")
    private Boolean isPrivate;
    private String comment;
    private LocalDateTime created;
    private LocalDateTime updated;
}
