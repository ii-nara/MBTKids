package com.ureca.config.auth;

import com.ureca.entity.AdminUserEntity;
import com.ureca.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {

  private final AdminUserRepository adminUserRepository;

  @Override
  public UserDetails loadUserByUsername(String adminLoginId) throws UsernameNotFoundException {
    AdminUserEntity admin =
        adminUserRepository
            .findByAdminLoginId(adminLoginId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 관리자입니다."));

    return new AdminDetails(admin);
  }
}
