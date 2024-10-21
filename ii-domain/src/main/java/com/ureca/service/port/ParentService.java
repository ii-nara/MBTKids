package com.ureca.service.port;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.entity.ParentEntity;

public interface ParentService {

  ParentEntity create(ParentSignUpRequestDto parentSignUpRequestDto);
}
