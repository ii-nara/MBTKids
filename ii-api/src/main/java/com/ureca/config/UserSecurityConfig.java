package com.ureca.config;

import com.ureca.config.auth.PrincipalDetailsService;
import com.ureca.config.oauth.CustomAuthenticationSuccessHandler;
import com.ureca.config.oauth.PrincipalOauth2UserService;
import com.ureca.repository.ParentRepository;
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
public class UserSecurityConfig {

  private final PrincipalOauth2UserService principalOauth2UserService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final ParentRepository parentRepository;

  @Bean
  @Order(2)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/mbtkids", "/mbtkids/register", "/mbtkids/login")
                    .permitAll()
                    .requestMatchers("/mbtkids/child/**")
                    .authenticated()
                    .requestMatchers("/mbtkids/home")
                    .authenticated()
                    .requestMatchers("/mbtkids/book/detail", "mbtkids/books")
                    .authenticated()
                    .requestMatchers("/mbtkids/mbti/**")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .formLogin(
            login ->
                login
                    .loginPage("/mbtkids")
                    .loginProcessingUrl("/mbtkids/login")
                    .usernameParameter("loginIdOrEmail")
                    .defaultSuccessUrl("/mbtkids/childSelectOrAdd", true)
                    .failureUrl("/mbtkids")
                    .permitAll())
        .oauth2Login(
            oauth ->
                oauth
                    .loginPage("/mbtkids/login")
                    .userInfoEndpoint(
                        userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(principalOauth2UserService))
                    .successHandler(customAuthenticationSuccessHandler))
        .logout(
            logout ->
                logout
                    .logoutUrl("/mbtkids/logout")
                    .logoutSuccessUrl("/mbtkids")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll())
        .sessionManagement(
            session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                    .expiredUrl("/mbtkids"));
    http.userDetailsService(new PrincipalDetailsService(parentRepository));

    return http.build();
  }
}
