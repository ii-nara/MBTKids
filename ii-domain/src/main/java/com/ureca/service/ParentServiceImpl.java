package com.ureca.service;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentJpaRepository;
import com.ureca.service.port.ParentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

  private final ParentJpaRepository parentJpaRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public ParentEntity create(ParentSignUpRequestDto parentSignUpRequestDto) {
    parentJpaRepository.findByParentLoginId(parentSignUpRequestDto.getParentLoginId())
        .ifPresent(parent -> {
          throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        });

    String encodedPwd = bCryptPasswordEncoder.encode(parentSignUpRequestDto.getPassword());
    ParentEntity parent = ParentEntity.createParent(
        parentSignUpRequestDto.getEmail(),
        parentSignUpRequestDto.getParentLoginId(),
        encodedPwd,
        parentSignUpRequestDto.getUserName(),
        parentSignUpRequestDto.getPhoneNumber(),
        parentSignUpRequestDto.getProvider(),
        LocalDateTime.now(),
        parentSignUpRequestDto.isInfoAgreeYn()
    );
    return parentJpaRepository.save(parent);
  }
}
