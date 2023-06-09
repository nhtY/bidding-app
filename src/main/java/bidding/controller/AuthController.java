package bidding.controller;

import bidding.utils.FormHandle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import bidding.dto.CurrentUser;
import bidding.model.User;
import bidding.dto.LoginForm;
import bidding.dto.MessageResponse;
import bidding.repository.UserRepository;
import bidding.security.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"} ) // http://localhost:3000/ for test the React
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
            return FormHandle.handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
        }

        if(userRepo.findByUsername(form.getUsername()) != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is already taken!"));
        }
        User userSaved = userRepo.save(form.toUser(passwordEncoder));

        log.info("User registered: {}", userSaved);

        return ResponseEntity.ok(userSaved);
    }

    @PostMapping(path="/login")
    public ResponseEntity authenticateUser(Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        return ResponseEntity.ok(new CurrentUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()));
    }

//    @PostMapping(path="/login")
//    public ResponseEntity authenticateUser(@Valid @RequestBody LoginForm loginData
//                                           // , BindingResult bindingResult
//                                                                                        ){
//
////        if (bindingResult.hasErrors()) {
////            log.warn("FORM VALIDATION ERROR...login");
////            return handleFormValidationError(bindingResult, HttpStatus.BAD_REQUEST);
////        }
//
//        Authentication authentication;
//        try {
//            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginData.getUsername(), loginData.getPassword()));
//        }catch (AuthenticationException exception) {
//            log.warn("Authentication exception : {}", exception.getMessage());
//            Map<String, String> authException = new LinkedHashMap<>();
//            authException.put("error", exception.getMessage());
//            return new ResponseEntity(authException, HttpStatus.BAD_REQUEST);
//        }
//
//
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        log.warn("AUTH DEATAILS: {}", authentication.isAuthenticated());
//
//        User user = (User) authentication.getPrincipal();
//        log.info("User Logged-in successful: {}", user);
//
//
//        log.info("AUTHED : {}", authentication.isAuthenticated());
//
//        return ResponseEntity.ok(new CurrentUser(
//                user.getId(),
//                user.getUsername(),
//                user.getFirstName(),
//                user.getLastName()));
//
//    }


//
//    @GetMapping("/current-user")
//    public ResponseEntity getCurrentUser(@AuthenticationPrincipal User userR) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if(authentication == null) {
//            log.warn("CURRENT USER is NULL !!!");
//            Map<String, String> body = new LinkedHashMap<>();
//            body.put("error", "No user found");
//            return new ResponseEntity(Optional.of(body), HttpStatus.NOT_FOUND);
//        }
//
//        log.warn("CURRENT USER getName: {}", authentication.getName());
//        User user = userRepo.findByUsername(userR.getUsername()); // authentication.getName()
//
//        return  ResponseEntity.ok(new CurrentUser(
//                user.getId(),
//                user.getUsername(),
//                user.getFirstName(),
//                user.getLastName()));
//    }

}