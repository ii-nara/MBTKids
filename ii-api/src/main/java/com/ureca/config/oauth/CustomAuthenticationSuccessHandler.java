package com.ureca.config.oauth;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final ParentRepository parentRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    String email = principalDetails.getParent().getEmail();

    ParentEntity parent =
        parentRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    if (parent.isActive()) {
      response.sendRedirect(request.getContextPath() + "/mbtkids/childSelectOrAdd");
    } else {
      response.sendRedirect(request.getContextPath() + "/mbtkids/oauth/additionalInfo");
    }
  }
}
