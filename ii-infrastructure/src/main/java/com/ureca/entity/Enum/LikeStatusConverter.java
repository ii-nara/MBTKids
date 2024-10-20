package com.ureca.entity.Enum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LikeStatusConverter implements AttributeConverter<LikeStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(LikeStatus status) {
    return status != null ? status.getValue() : null;
  }

  @Override
  public LikeStatus convertToEntityAttribute(Integer value) {
    return value != null ? LikeStatus.fromValue(value) : null;
  }
}

