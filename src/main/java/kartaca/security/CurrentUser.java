package kartaca.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUser {
    private String userID;
    private String username;
    private String firstName;
    private String lastName;

}
