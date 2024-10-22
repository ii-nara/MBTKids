package com.ureca.service;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentJpaRepository;
import com.ureca.service.port.ParentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

  private final ParentJpaRepository parentJpaRepository;

  @Override
  public ParentEntity create(ParentSignUpRequestDto parentSignUpRequestDto) {
    parentJpaRepository.findByParentLoginId(parentSignUpRequestDto.getParentLoginId())
        .ifPresent(parent -> {
          throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        });

    ParentEntity parent = ParentEntity.createParent(
        parentSignUpRequestDto.getEmail(),
        parentSignUpRequestDto.getParentLoginId(),
        parentSignUpRequestDto.getPassword(),
        parentSignUpRequestDto.getUserName(),
        parentSignUpRequestDto.getPhoneNumber(),
        parentSignUpRequestDto.getProvider(),
        LocalDateTime.now(),
        parentSignUpRequestDto.isInfoAgreeYn()
    );
    return parentJpaRepository.save(parent);
  }
}
