package co.com.jestma.model.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private Integer code;
    private String status;
    private String message;
    private T body;
    private String idTransaction;
}
