package co.com.jestma.model.randomnumber;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class RandomNumber {
    private List<Integer> numbers;
}
