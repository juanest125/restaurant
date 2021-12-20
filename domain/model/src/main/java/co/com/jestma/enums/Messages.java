package co.com.jestma.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {
    SUCCESS("success");

    private final String id;
}
