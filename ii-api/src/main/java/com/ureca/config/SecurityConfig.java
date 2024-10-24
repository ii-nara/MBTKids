package com.ureca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/mbtkids", "/mbtkids/register", "/mbtkids/login").permitAll()
            .requestMatchers("/mbtkids/child/**").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(login -> login
            .loginPage("/mbtkids/login")
            .loginProcessingUrl("/mbtkids/login")
            .usernameParameter("loginIdOrEmail")
            .defaultSuccessUrl("/mbtkids/childSelectOrAdd", true)
            .failureUrl("/mbtkids")
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/mbtkids/logout")
            .logoutSuccessUrl("/mbtkids")
            .permitAll());

    return http.build();
  }
}
