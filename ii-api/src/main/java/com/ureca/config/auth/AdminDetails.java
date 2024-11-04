package com.ureca.config.auth;

import com.ureca.entity.AdminUserEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Data
public class AdminDetails implements UserDetails, Serializable {

  private static final long serialVersionUID = 1L;

  private final AdminUserEntity admin;

  public AdminDetails(AdminUserEntity admin) {
    this.admin = admin;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return admin.getPassword();
  }

  @Override
  public String getUsername() {
    return admin.getAdminLoginId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
