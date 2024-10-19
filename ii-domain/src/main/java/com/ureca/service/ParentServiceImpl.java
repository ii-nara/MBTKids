package com.ureca.service;

import com.ureca.domain.Parent;
import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.service.port.ParentRepository;
import com.ureca.service.port.ParentService;
import org.springframework.stereotype.Service;

@Service
public class ParentServiceImpl implements ParentService {

  private final ParentRepository parentRepository;

  public ParentServiceImpl(ParentRepository parentRepository) {
    this.parentRepository = parentRepository;
  }

  @Override
  public Parent create(ParentSignUpRequestDto parentSignUpRequestDto) {
    parentRepository.findByParentLoginId(parentSignUpRequestDto.getParentLoginId())
        .ifPresent(parent -> {
          throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        });

    Parent parent = Parent.from(parentSignUpRequestDto);
    return parentRepository.save(parent);
  }
}
