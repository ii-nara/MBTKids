package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChildCreateDto {

  private final String childName;
  private final int childAge;
}
