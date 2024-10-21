package com.ureca.service.port;

import com.ureca.domain.Parent;
import com.ureca.dto.ParentSignUpRequestDto;

public interface ParentService {

  Parent create(ParentSignUpRequestDto parentSignUpRequestDto);
}
