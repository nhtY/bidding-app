package kartaca.controller;

import jakarta.validation.Valid;
import kartaca.dto.CurrentUser;
import kartaca.model.User;
import kartaca.dto.LoginForm;
import kartaca.dto.MessageResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
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
        User userSaved = userRepo.save(form.toUser(passwordEncoder));

        log.info("User registered: {}", userSaved);

        return ResponseEntity.ok(userSaved);
    }

    @PostMapping(path="/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginForm loginData
                                           // , BindingResult bindingResult
                                                                                        ){

//        if (bindingResult.hasErrors()) {
//            log.warn("FORM VALIDATION ERROR...login");
//            return handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
//        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginData.getUsername(), loginData.getPassword()));
        }catch (AuthenticationException exception) {
            log.warn("Authentication exception : {}", exception.getMessage());
            Map<String, String> authException = new LinkedHashMap<>();
            authException.put("error", exception.getMessage());
            return new ResponseEntity(authException, HttpStatus.BAD_REQUEST);
        }



        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.warn("AUTH DEATAILS: {}", authentication.isAuthenticated());

        User user = (User) authentication.getPrincipal();
        log.info("User Logged-in successful: {}", user);


        log.info("AUTHED : ", authentication.isAuthenticated());

        return ResponseEntity.ok(new CurrentUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()));

    }



    @GetMapping("/current-user")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal User userR) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            log.warn("CURRENT USER is NULL !!!");
            Map<String, String> body = new LinkedHashMap<>();
            body.put("error", "No user found");
            return new ResponseEntity(Optional.of(body), HttpStatus.NOT_FOUND);
        }

        log.warn("CURRENT USER getName: {}", authentication.getName());
        User user = userRepo.findByUsername(userR.getUsername()); // authentication.getName()

        return  ResponseEntity.ok(new CurrentUser(
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
