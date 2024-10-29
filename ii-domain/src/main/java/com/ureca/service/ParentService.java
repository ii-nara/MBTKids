package com.ureca.service;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.dto.ReqParentAddInfoDto;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ParentRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentService {

  private final ParentRepository parentRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public ParentEntity create(ParentSignUpRequestDto parentSignUpRequestDto) {
    parentRepository
        .findByParentLoginId(parentSignUpRequestDto.getParentLoginId())
        .ifPresent(
            parent -> {
              throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
            });

    String encodedPwd = bCryptPasswordEncoder.encode(parentSignUpRequestDto.getPassword());
    ParentEntity parent =
        ParentEntity.createParent(
            parentSignUpRequestDto.getEmail(),
            parentSignUpRequestDto.getParentLoginId(),
            encodedPwd,
            parentSignUpRequestDto.getUserName(),
            parentSignUpRequestDto.getPhoneNumber(),
            parentSignUpRequestDto.getProvider(),
            LocalDateTime.now(),
            parentSignUpRequestDto.isInfoAgreeYn());
    return parentRepository.save(parent);
  }

  public void saveAdditionalInfo(ReqParentAddInfoDto parentAddInfoDto, ParentEntity parent) {
    parent.updateAdditionalInfo(
        parentAddInfoDto.getUserName(),
        parentAddInfoDto.getPhoneNumber(),
        parentAddInfoDto.isInfoAgreeYn());
    parentRepository.save(parent);
  }
}
