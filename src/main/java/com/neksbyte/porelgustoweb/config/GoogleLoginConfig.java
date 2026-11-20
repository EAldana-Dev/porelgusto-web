package com.neksbyte.porelgustoweb.config;

import com.neksbyte.porelgustoweb.dto.auth.AuthResponse;
import com.neksbyte.porelgustoweb.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@Profile("google")
public class GoogleLoginConfig {
    @Bean
    SecurityFilterChain webSecurity(HttpSecurity http, AuthService authService) throws Exception {
        AuthenticationSuccessHandler googleSuccess = (request, response, authentication) -> {
            var principal = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
            String email = principal.getAttribute("email");
            Boolean verified = principal.getAttribute("email_verified");
            if (email == null || !Boolean.TRUE.equals(verified)) {
                response.sendRedirect("/login?googleError");
                return;
            }
            AuthResponse auth = authService.loginConGoogle(email, principal.getAttribute("name"));
            HttpSession session = request.getSession(true);
            session.setAttribute("authToken", auth.token());
            session.setAttribute("authTokenType", auth.tokenType());
            session.setAttribute("authUsuario", auth.usuario());
            response.sendRedirect("/home");
        };

        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/login"))
                .oauth2Login(oauth -> oauth.loginPage("/login").successHandler(googleSuccess))
                .logout(Customizer.withDefaults());
        return http.build();
    }
}
