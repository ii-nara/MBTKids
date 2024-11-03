package com.ureca.config.auth;

import com.ureca.entity.ChildEntity;
import com.ureca.entity.ParentEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Data
public class PrincipalDetails implements UserDetails, OAuth2User, Serializable {

  private static final long serialVersionUID = 1L;
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

  public void setChild(ChildEntity child) {
    this.child = child;

    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(
                  this, auth.getCredentials(), auth.getAuthorities()));
    }
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

  public Long getChildId() {
    return child != null ? child.getChildId() : null;
  }

  public void clearChild() {
    this.child = null;
  }
}
