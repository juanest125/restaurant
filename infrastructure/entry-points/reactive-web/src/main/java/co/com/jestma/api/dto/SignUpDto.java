package co.com.jestma.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {
    private String name;
    private String lastName;
    private String email;
    private String password;
}
