package kartaca.controller;

import jakarta.validation.Valid;
import kartaca.model.LoginResponse;
import kartaca.model.User;
import kartaca.security.LoginForm;
import kartaca.model.MessageResponse;
import kartaca.repository.UserRepository;
import kartaca.security.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping( "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MessageResponse> registerUser(@RequestBody @Valid RegisterForm form) {
        if(userRepo.existsByUsername(form.getUsername())) {
            ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        userRepo.save(form.toUser(passwordEncoder));

        log.info("User registered: {}", form);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping(path="/login", consumes="application/json")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginForm loginData){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginData.getUsername(), loginData.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User Logged-in successful: {}", loginData);

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()));
    }
}
