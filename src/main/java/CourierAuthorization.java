import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierAuthorization {
    private String login;
    private String password;

    public static CourierAuthorization from(Courier courier) {
        return new CourierAuthorization(courier.getLogin(), courier.getPassword());
    }
}