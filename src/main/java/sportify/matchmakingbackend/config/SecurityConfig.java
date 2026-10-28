package sportify.matchmakingbackend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API за мобилна апликација: без сесии, без CSRF (токенот оди во header)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // CORS preflight од browser-от
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // register и login се јавни
                        .requestMatchers("/api/auth/register/**", "/api/auth/login").permitAll()
                        // разгледување (GET) е јавно
                        .requestMatchers(HttpMethod.GET, "/api/sports/**", "/api/venues/**", "/api/matches/**").permitAll()
                        // менување спортови: само админ
                        .requestMatchers("/api/sports/**").hasRole("ADMIN")
                        // креирање/менување терени: само партнер (сопственик)
                        .requestMatchers("/api/venues/**").hasRole("OWNER")
                        // креирање, join, leave, cancel натпревар: само играч
                        .requestMatchers(HttpMethod.POST, "/api/matches/**").hasRole("USER")
                        // сè останато (/api/auth/me, /api/players/**...) бара најава
                        .anyRequest().authenticated())
                // Spring сам го проверува "Authorization: Bearer <token>" со JwtDecoder-от од JwtConfig
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint((request, response, ex) ->
                                writeError(response, HttpStatus.UNAUTHORIZED, "Authentication required"))
                        .accessDeniedHandler((request, response, ex) ->
                                writeError(response, HttpStatus.FORBIDDEN, "You don't have permission for this action")));

        return http.build();
    }

    // "roles": ["ROLE_USER"] од токенот → authorities што ги разбира hasRole("USER")
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix(""); // улогите веќе имаат "ROLE_" во токенот

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    // потребно за Expo Web / browser debugging; native апликацијата не праќа CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Location"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 401/403 во истиот JSON формат како GlobalExceptionHandler (ApiError),
    // бидејќи овие грешки настануваат во security филтерот, пред контролерите
    private static void writeError(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("""
                {"status":%d,"error":"%s","message":"%s","fieldErrors":{},"timestamp":"%s"}"""
                .formatted(status.value(), status.getReasonPhrase(), message, LocalDateTime.now()));
    }
}