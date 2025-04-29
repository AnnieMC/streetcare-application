package app_reporting_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
//                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
//                .authorizeHttpRequests(auth -> auth
//                        // Public endpoints
//                        .requestMatchers(HttpMethod.POST, "/api/user", "/api/user/login").permitAll()
//
//                        // Protected endpoints
//                        .requestMatchers(HttpMethod.GET, "/api/user", "/api/user/{id}").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/api/user/logout").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/api/pothole").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/api/feedback").authenticated()
//
//                        // Any other request must be authenticated
//                        .anyRequest().authenticated()
//                );

                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()
                );

//                (HttpMethod.GET, "/api/user", "/api/user/{id}", "/api/pothole", "/api/pothole/{id}").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/user/login", "/api/user", "/api/pothole", "/api/feedback").permitAll()
//                        .anyRequest().authenticated()


        return http.build();
    }
}
