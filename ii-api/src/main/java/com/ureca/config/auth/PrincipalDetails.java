package com.ureca.config.auth;

import com.ureca.entity.ChildEntity;
import com.ureca.entity.ParentEntity;
import java.util.Collection;
import java.util.Collections;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Data
public class PrincipalDetails implements UserDetails {

  private final ParentEntity parent;

  private ChildEntity child;

  public PrincipalDetails(ParentEntity parent) {
    this.parent = parent;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return parent.getPassword();
  }

  @Override
  public String getUsername() {
    return parent.getParentLoginId();
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
