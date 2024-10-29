package com.ureca.config.oauth;

import com.ureca.config.CustomBCryptPasswordEncoder;
import com.ureca.config.auth.PrincipalDetails;
import com.ureca.config.oauth.provider.GoogleUserInfo;
import com.ureca.config.oauth.provider.OAuth2UserInfo;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

  private final CustomBCryptPasswordEncoder bCryptPasswordEncoder;

  private final ParentRepository parentRepository;


  @Override
  public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
    System.out.println("userRequest : " + req.getClientRegistration());
    System.out.println("getAccessToken : " + req.getAccessToken().getTokenValue());

    OAuth2User oAuth2User = super.loadUser(req);
    System.out.println("getAttributes : " + oAuth2User.getAttributes());

    OAuth2UserInfo oAuth2UserInfo = null;

    if (req.getClientRegistration().getRegistrationId().equals("google")) {
      System.out.println("구글 로그인 요청");
      oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
    } else if (req.getClientRegistration().getRegistrationId().equals("kakao")) {
      System.out.println("카카오 로그인 요청");
//      oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
    }

    String provider = oAuth2UserInfo.getProvider();
    String providerId = oAuth2UserInfo.getProviderId();
    String loginId = provider + "_" + providerId;
    String username = oAuth2UserInfo.getName();
    String password = bCryptPasswordEncoder.encode("소셜로그인");
    String email = oAuth2UserInfo.getEmail();

    Optional<ParentEntity> parentOpt = parentRepository.findByParentLoginId(loginId);

    ParentEntity parent = null;
    if (parentOpt.isEmpty()) {
      parent = ParentEntity.builder()
          .parentLoginId(loginId)
          .email(email)
          .password(password)
          .userName(username)
          .provider(provider)
          .createdAt(LocalDateTime.now())
          .isActive(false)
          .build();
      parentRepository.save(parent);
    }
    return new PrincipalDetails(parent, oAuth2User.getAttributes());
  }
}
