package com.ureca.entity.Enum;

public enum LikeStatus {
  DISLIKE(-1),
  NONE(0),
  LIKE(1);

  private int value;

  LikeStatus(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static LikeStatus fromValue(int value) {
    for (LikeStatus status : LikeStatus.values()) {
      if (status.getValue() == value) {
        return status;
      }
    }
    throw new IllegalArgumentException("유효하지 않는 숫자 : " + value);
  }
}
