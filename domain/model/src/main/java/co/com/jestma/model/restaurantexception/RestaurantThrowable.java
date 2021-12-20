package co.com.jestma.model.restaurantexception;

import lombok.*;

//@Data
//@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
//@NoArgsConstructor
public class RestaurantThrowable extends Throwable {
    private final String code;
    public RestaurantThrowable(String code, String message){
        super(message);
        this.code = code;
    }
}
