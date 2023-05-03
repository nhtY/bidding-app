package bidding.security;

import bidding.model.User;
import bidding.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//        requestCache.setMatchingRequestParameterName("continue");

        // http.cors().disable().csrf().disable().authorizeHttpRequests().anyRequest().permitAll();

        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        http
            .cors().disable()
            .csrf().disable()
            .authorizeHttpRequests((request) -> request
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/products").permitAll()
                    .requestMatchers("/api/products/**").hasRole("USER")
                    .anyRequest().authenticated()
                )
        .logout()
        .logoutUrl("/api/auth/logout")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
        .and()
        // Use HttpBasic Authentication
        .httpBasic((basic) -> basic // save logged-in user's session data in context repository
                .withObjectPostProcessor(new ObjectPostProcessor<BasicAuthenticationFilter>() {
                    public BasicAuthenticationFilter postProcess(BasicAuthenticationFilter filter) {
                        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
                        return filter;
                    }
                }))
        .anonymous().disable();



        return  http.build();
    }

}
