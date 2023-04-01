package kartaca.security;

import kartaca.model.User;
import kartaca.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class SecurityConfig {

    // Define password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            User user = userRepo.findByUsername(username);

            if(user != null) {
                log.info("USER FOUND: {}", user);
                return user;
            }

            log.warn("USER NOT FOUND !!");

            throw new UsernameNotFoundException("User with '" + username + "' NOT FOUND");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(                        (request) -> request
                .requestMatchers("/", "/**").hasRole("USER")
                .requestMatchers("/login", "/register").permitAll())
                .formLogin()
                .defaultSuccessUrl("/", true);

        return  http.build();
    }

}
