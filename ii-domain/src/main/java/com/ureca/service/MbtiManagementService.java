package com.ureca.service;

import com.ureca.dto.MbtiStatusResponseDto;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.entity.MbtiStatusEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiManagementService {

  private MbtiStatusResponseDto createMbtiStatusResponseDto(int typeIE, int typeSN, int typeTF,
      int typePJ) {
    int typeE = typeIE;
    int typeI = 11 - typeIE;
    int typeN = typeSN;
    int typeS = 11 - typeSN;
    int typeF = typeTF;
    int typeT = 11 - typeTF;
    int typeJ = typePJ;
    int typeP = 11 - typePJ;

    String MbtiType = "";
    MbtiType += (typeE > typeI) ? "E" : "I";
    MbtiType += (typeN > typeS) ? "N" : "S";
    MbtiType += (typeF > typeT) ? "F" : "T";
    MbtiType += (typeJ > typeP) ? "J" : "P";

    return MbtiStatusResponseDto.builder()
        .typeE(typeE)
        .typeI(typeI)
        .typeN(typeN)
        .typeS(typeS)
        .typeF(typeF)
        .typeT(typeT)
        .typeJ(typeJ)
        .typeP(typeP)
        .MbtiType(MbtiType)
        .build();
  }

  public MbtiStatusResponseDto getMbtiStatus(ChildEntity child) {
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();

    return createMbtiStatusResponseDto(mbtiStatus.getTypeIE(), mbtiStatus.getTypeSN(),
        mbtiStatus.getTypeTF(), mbtiStatus.getTypePJ());
  }

  public List<MbtiStatusResponseDto> getMbtiHistory(ChildEntity child) {
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    List<MbtiHistoryEntity> mbtiHistoryEntityList = mbtiStatus.getMbtiHistoryEntities();

    List<MbtiStatusResponseDto> mbtiHistoryDtoList = new ArrayList<>();

    for (MbtiHistoryEntity mbtiHistoryEntity : mbtiHistoryEntityList) {
      mbtiHistoryDtoList.add(
          createMbtiStatusResponseDto(mbtiHistoryEntity.getTypeIE(), mbtiHistoryEntity.getTypeSN(),
              mbtiHistoryEntity.getTypeTF(), mbtiHistoryEntity.getTypePJ()));
    }

    return mbtiHistoryDtoList;
  }

  public void deleteMbtiLogical(ChildEntity child) {
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    mbtiStatus.setDeleteAt(LocalDateTime.now());
    child.setMbtiStatusEntity(null);
  }
}
