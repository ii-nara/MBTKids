package com.ureca.config;

import com.ureca.config.auth.AdminDetailsService;
import com.ureca.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AdminSecurityConfig {

  private final AdminUserRepository adminUserRepository;

  @Bean
  @Order(1)
  public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/mbtkids/admin/**");
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/mbtkids/admin/login")
                    .permitAll()
                    .requestMatchers("/mbtkids/admin/**")
                    .authenticated())
        .formLogin(
            login ->
                login
                    .loginPage("/mbtkids/admin")
                    .loginProcessingUrl("/mbtkids/admin/login")
                    .usernameParameter("adminLoginId")
                    .defaultSuccessUrl("/mbtkids/admin/home", true)
                    .failureUrl("/mbtkids/admin")
                    .permitAll())
        .logout(
            logout ->
                logout
                    .logoutUrl("/mbtkids/admin/logout")
                    .logoutSuccessUrl("/mbtkids/admin")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll())
        .sessionManagement(
            //session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
            session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                    .expiredUrl("/mbtkids/admin"));

    http.userDetailsService(new AdminDetailsService(adminUserRepository));

    return http.build();
  }
}
