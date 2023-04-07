package kartaca.security;


import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
}