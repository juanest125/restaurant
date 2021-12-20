package co.com.jestma.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class LoginResponseDto {
    private String id;
    private String token;
    private String userId;
    private LocalDateTime created;
}
