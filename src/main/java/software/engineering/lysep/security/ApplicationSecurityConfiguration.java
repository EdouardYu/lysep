package software.engineering.lysep.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import software.engineering.lysep.dto.ErrorEntity;

@EnableMethodSecurity
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "/activate").permitAll()
                .requestMatchers(HttpMethod.POST, "/activate/new").permitAll()
                .requestMatchers(HttpMethod.POST, "/signin").permitAll()
                .requestMatchers(HttpMethod.POST, "/password/reset").permitAll()
                .requestMatchers(HttpMethod.POST, "/password/new").permitAll()
                .anyRequest().authenticated()
            ).exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json;charset=UTF-8");
                        String authorization = request.getHeader("Authorization");
                        ErrorEntity errorEntity;

                        if (authorization != null && authorization.startsWith("Bearer ")) {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            errorEntity = new ErrorEntity(
                                HttpServletResponse.SC_FORBIDDEN,
                                "Access is denied"
                            );
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            errorEntity = new ErrorEntity(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                "Authentication is required"
                            );
                        }

                        response.getWriter().write(this.objectMapper.writeValueAsString(errorEntity));
                    })
            )
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                    httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
