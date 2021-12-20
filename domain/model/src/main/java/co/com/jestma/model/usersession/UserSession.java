package co.com.jestma.model.usersession;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class UserSession {
    private String id;
    private String token;
    private String userId;
    private LocalDateTime created;
}
