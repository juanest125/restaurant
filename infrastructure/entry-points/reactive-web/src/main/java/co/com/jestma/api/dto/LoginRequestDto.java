package co.com.jestma.api.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
