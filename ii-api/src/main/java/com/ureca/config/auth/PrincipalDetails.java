package com.ureca.config.auth;

import com.ureca.entity.ChildEntity;
import com.ureca.entity.ParentEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

  private final ParentEntity parent;
  private Map<String, Object> attributes;
  private ChildEntity child;

  public PrincipalDetails(ParentEntity parent) {
    this.parent = parent;
  }

  public PrincipalDetails(ParentEntity parent, Map<String, Object> attributes) {
    if (parent == null) {
      throw new IllegalArgumentException("Parent entity cannot be null");
    }
    this.parent = parent;
    this.attributes = attributes;
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

  @Override
  public Map<String, Object> getAttribute(String name) {
    return attributes;
  }

  @Override
  public String getName() {
    return null;
  }
}
