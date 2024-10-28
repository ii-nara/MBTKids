package com.ureca.dto;

import lombok.Data;

// WBK0111-도서수정, WBK0200-도서등록 화면 AI 성향 분석 응답 DTO
@Data
public class ResMbtiInfo {

  // 성향
  private String mbtiType;
  // 앞 : -1, 뒤 : 1, 상태없음 : 0
  // I/E
  private int typeIE;
  // S/N
  private int typeSN;
  // T/F
  private int typeTF;
  // P/J
  private int typePJ;

  // 생성자
  public ResMbtiInfo() {}

  public ResMbtiInfo(String mbtiType, int typeIE, int typeSN, int typeTF, int typePJ) {
    this.mbtiType = mbtiType;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
  }
}
