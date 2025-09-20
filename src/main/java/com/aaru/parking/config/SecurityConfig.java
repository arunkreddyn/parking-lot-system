package com.aaru.parking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.admin.emails:}")
    private String adminEmails;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Explicitly allow H2-Console using AntPathRequestMatcher
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        // Other public endpoints can also use AntPathRequestMatcher
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/auth/login")).permitAll()

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/**")).hasRole("ADMIN")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/", true))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }


    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User oauth2User = delegate.loadUser(request);
            String email = (String) oauth2User.getAttributes().get("email");
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (isAdmin(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            Map<String, Object> attrs = new HashMap<>(oauth2User.getAttributes());
            String nameKey = attrs.containsKey("sub") ? "sub" : "email";
            return new DefaultOAuth2User(authorities, attrs, nameKey);
        };
    }

    private boolean isAdmin(String email) {
        if (email == null || adminEmails.isBlank()) return false;
        List<String> admins = Arrays.stream(adminEmails.split(",")).map(String::trim).toList();
        return admins.contains(email);
    }
}
