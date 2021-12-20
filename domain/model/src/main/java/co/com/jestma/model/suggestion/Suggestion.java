package co.com.jestma.model.suggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Suggestion {
    private String id;
    private String userId;
    private Boolean isPrivate;
    private String comment;
    private LocalDateTime created;
    private LocalDateTime updated;
}
