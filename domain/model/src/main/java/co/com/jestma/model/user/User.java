package co.com.jestma.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime created;
    private LocalDateTime updated;
}
