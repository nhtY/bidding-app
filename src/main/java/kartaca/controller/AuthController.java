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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.warn("FORM VALIDATION ERROR...register");
            return handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
        }

        if(userRepo.existsByUsername(form.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        userRepo.save(form.toUser(passwordEncoder));

        log.info("User registered: {}", form);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping(path="/login", consumes="application/json")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginForm loginData
                                                   // , BindingResult bindingResult
                                                                                        ){

//        if (bindingResult.hasErrors()) {
//            log.warn("FORM VALIDATION ERROR...login");
//            return handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
//        }

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

    private  ResponseEntity<Object> handleFormValidationError(BindingResult bindingResult, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status);

        //Get all fields errors
        List<String> errors = bindingResult
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatusCode.valueOf(status.value()));
    }
}
