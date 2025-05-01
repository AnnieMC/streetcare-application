package app_reporting_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class StreetcareBackendApplication {

	public static void main(String[] args) {
		//System.out.println("Hello World");
		SpringApplication.run(StreetcareBackendApplication.class, args);
	}

	//Code copied from
	// "Dimitri, M. (2019). Enabling CORS Globally in Spring Boot. [online]
	// Stack Overflow. Available at:
	// https://stackoverflow.com/questions/51720552/enabling-cors-globally-in-spring-boot."
	// CORS configuration to allow requests from the frontend
	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// Don't do this in production, use a proper list  of allowed origins
		config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));//Specify the frontend URL
		config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
