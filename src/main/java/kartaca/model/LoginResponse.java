package kartaca.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String userID;
    private String username;
    private String firstName;
    private String lastName;

}
