package kartaca.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kartaca.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegisterForm {

    @NotBlank(message = "İsim boş bırakılamaz")
    @Length(min = 3, message = "İsim en az 3 karakter olmalı")
    private String firstName;

    @NotBlank(message = "Soyisim boş bırakılamaz")
    @Length(min = 3, message = "Soyisim en az 3 karakter olmalı")
    private String lastName;

    @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
    @Length(min = 4, message = "Kullanıcı adı en az 4 karakter olmalı")
    private String username;

    @NotBlank(message = "Şifre boş bırakılamaz")
    @Length(min = 8, message = "Şifre en az 8 karakter olmalı")
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.of(firstName, lastName, username, passwordEncoder.encode(password));
    }

}
