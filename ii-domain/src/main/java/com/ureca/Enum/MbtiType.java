package com.ureca.Enum;

public enum MbtiType {
  TYPE_IE(-1, 1), // I는 -1, E는 1
  TYPE_SN(-1, 1), // S는 -1, N은 1
  TYPE_TF(-1, 1), // T는 -1, F는 1
  TYPE_PJ(-1, 1); // P는 -1, J는 1

  private final int value1;
  private final int value2;

  MbtiType(int value1, int value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public int getValue1() {
    return value1;
  }

  public int getValue2() {
    return value2;
  }

  public int getValueForType(String type) {
    switch (type) {
      case "I":
        return -1;
      case "E":
        return 1;
      case "S":
        return -1;
      case "N":
        return 1;
      case "T":
        return -1;
      case "F":
        return 1;
      case "P":
        return -1;
      case "J":
        return 1;
      case "0": // "0"인 경우 추가
        return 0;
      default:
        return 0; // 정의되지 않은 경우
    }
  }
}
