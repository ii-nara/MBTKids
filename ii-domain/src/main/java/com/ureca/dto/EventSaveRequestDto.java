package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EventSaveRequestDto {

  private String name;

  private String phone;
}
