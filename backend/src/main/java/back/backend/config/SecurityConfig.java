package back.backend.config;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
        .cors( cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests()
        .requestMatchers("/api/auth/**")
        .permitAll()
        .requestMatchers("/api/form/getform/**")
        .permitAll()
        .requestMatchers("/api/form/delete/**")
        .permitAll()
        .requestMatchers("/api/email/send")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
    ;

    return http.build();
  }

  // configure CORS policy
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081")); // add your allowed origins here
    configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
