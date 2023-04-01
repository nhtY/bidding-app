package kartaca.controller;

import jakarta.validation.Valid;
import kartaca.model.User;
import kartaca.repository.UserRepository;
import kartaca.security.RegisterForm;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid RegisterForm form, Errors error) {
        return userRepo.save(form.toUser(passwordEncoder));
    }
}
