package com.ureca.config;

import com.ureca.config.oauth.CustomAuthenticationSuccessHandler;
import com.ureca.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class UserSecurityConfig {

  private final PrincipalOauth2UserService principalOauth2UserService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/mbtkids", "/mbtkids/register", "/mbtkids/login").permitAll()
            .requestMatchers("/mbtkids/child/**").authenticated()
            .requestMatchers("/mbtkids/childSelectOrAdd").authenticated()
            .anyRequest().permitAll()
        )
        .formLogin(login -> login
            .loginPage("/mbtkids/login")
            .loginProcessingUrl("/mbtkids/login")
            .usernameParameter("loginIdOrEmail")
            .defaultSuccessUrl("/mbtkids/childSelectOrAdd", true)
            .failureUrl("/mbtkids")
            .permitAll())
        .oauth2Login(oauth -> oauth
            .loginPage("/mbtkids/login")
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                .userService(principalOauth2UserService))
            .successHandler(customAuthenticationSuccessHandler)
        )
        .logout(logout -> logout
            .logoutUrl("/mbtkids/logout")
            .logoutSuccessUrl("/mbtkids")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll());

    return http.build();
  }
}
