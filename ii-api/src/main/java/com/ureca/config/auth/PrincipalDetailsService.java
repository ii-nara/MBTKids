package com.ureca.config.auth;

import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

  private final ParentRepository parentRepository;

  @Override
  public UserDetails loadUserByUsername(String loginIdOrEmail) throws UsernameNotFoundException {
    ParentEntity parent;
    if (loginIdOrEmail.contains("@")) {
      parent =
          parentRepository
              .findByEmail(loginIdOrEmail)
              .orElseThrow(() -> new IllegalArgumentException("아이디 또는 이메일을 확인해주세요."));
    } else {
      parent =
          parentRepository
              .findByParentLoginId(loginIdOrEmail)
              .orElseThrow(() -> new IllegalArgumentException("아이디 또는 이메일을 확인해주세요."));
    }
    return new PrincipalDetails(parent);
  }
}
