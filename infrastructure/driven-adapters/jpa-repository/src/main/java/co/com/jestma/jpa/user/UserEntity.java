package co.com.jestma.jpa.user;

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
@Table(name = "user_account")
public class UserEntity {
    @Id
    private String id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime created;
    private LocalDateTime updated;
}

