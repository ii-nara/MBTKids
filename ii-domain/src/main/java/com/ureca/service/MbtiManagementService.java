package com.ureca.service;

import com.ureca.dto.MbtiHistoryResponseDto;
import com.ureca.dto.MbtiStatusRequestDto;
import com.ureca.dto.MbtiStatusResponseDto;
import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.entity.MbtiStatusEntity;
import com.ureca.repository.ChildRepository;
import com.ureca.repository.MbtiStatusRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbtiManagementService {

  private final int MAX_SCORE = 11;
  private final MbtiStatusRepository mbtiStatusRepository;
  private final ChildRepository childRepository;

  // toDTO
  private MbtiStatusResponseDto createMbtiStatusResponseDto(int typeE, int typeN, int typeF,
      int typeJ) {
    int typeI = MAX_SCORE - typeE;
    int typeS = MAX_SCORE - typeN;
    int typeT = MAX_SCORE - typeF;
    int typeP = MAX_SCORE - typeJ;

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

  // 0. 자녀 조회
  public ChildEntity getChild(Long childId) {
    ChildEntity childEntity = childRepository.findById(childId)
        .orElseThrow(() -> new RuntimeException("ID: " + childId + "의 자녀를 찾을 수 없습니다."));
    return childEntity;
  }

  // 1. 조회 (성향) -> 아이디로
  // 아이디로 하는 이유 : 로그인할 때엔 성향이 Null인 상태 -> 조회를 해줘야 업데이트된 Status를 가져올 수 있음!
  public MbtiStatusResponseDto getMbtiStatus(Long childId) {
    ChildEntity child = getChild(childId);
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    return createMbtiStatusResponseDto(mbtiStatus.getTypeIE(), mbtiStatus.getTypeSN(),
        mbtiStatus.getTypeTF(), mbtiStatus.getTypePJ());
  }

  // 2. 조회 (히스토리)
  public List<MbtiHistoryResponseDto> getMbtiHistory(Long childId, LocalDate startDate,
      LocalDate endDate) {
    ChildEntity child = getChild(childId);
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    List<MbtiHistoryEntity> mbtiHistoryEntityList = mbtiStatus.getMbtiHistoryEntities();

    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

    List<MbtiHistoryResponseDto> mbtiHistoryDtoList = new ArrayList<>();

    for (MbtiHistoryEntity mbtiHistoryEntity : mbtiHistoryEntityList) {
      LocalDateTime updateAt = mbtiHistoryEntity.getTimeStamp();

      if (updateAt.isBefore(startDateTime) || updateAt.isAfter(endDateTime)) {
        continue;
      }

      MbtiStatusResponseDto mbtiStatusResponseDto = createMbtiStatusResponseDto(
          mbtiHistoryEntity.getTypeIE(),
          mbtiHistoryEntity.getTypeSN(), mbtiHistoryEntity.getTypeTF(),
          mbtiHistoryEntity.getTypePJ());

      MbtiHistoryResponseDto mbtiHistoryResponseDto = MbtiHistoryResponseDto.builder()
          .typeI(mbtiStatusResponseDto.getTypeI())
          .typeE(mbtiStatusResponseDto.getTypeE())
          .typeS(mbtiStatusResponseDto.getTypeS())
          .typeN(mbtiStatusResponseDto.getTypeN())
          .typeF(mbtiStatusResponseDto.getTypeF())
          .typeT(mbtiStatusResponseDto.getTypeT())
          .typeJ(mbtiStatusResponseDto.getTypeJ())
          .typeP(mbtiStatusResponseDto.getTypeP())
          .MbtiType(mbtiStatusResponseDto.getMbtiType())
          .updateAt(updateAt)
          .build();

      mbtiHistoryDtoList.add(mbtiHistoryResponseDto);
    }
    return mbtiHistoryDtoList;
  }

  // 3. 등록 (성향)
  public MbtiStatusResponseDto insertMbtiStatus(Long childId,
      MbtiStatusRequestDto mbtiStatusReqDto) {
    ChildEntity child = getChild(childId);
    // 성향 등록
    MbtiStatusEntity mbtiStatus = MbtiStatusEntity.builder()
        .typeIE(mbtiStatusReqDto.getScoreIE())
        .typeSN(mbtiStatusReqDto.getScoreSN())
        .typeTF(mbtiStatusReqDto.getScoreTF())
        .typePJ(mbtiStatusReqDto.getScorePJ())
        .childEntity(child)
        .build();
    MbtiHistoryEntity mbtiHistoryEntity = MbtiHistoryEntity.builder()
        .typeIE(mbtiStatusReqDto.getScoreIE())
        .typeSN(mbtiStatusReqDto.getScoreSN())
        .typeTF(mbtiStatusReqDto.getScoreTF())
        .typePJ(mbtiStatusReqDto.getScorePJ())
        .build();
    // 히스토리 등록
    mbtiStatus.addHistory(mbtiHistoryEntity);
    mbtiStatusRepository.save(mbtiStatus);
    // 자녀 업데이트
    child.setMbtiStatusEntity(mbtiStatus);
    childRepository.save(child);
    // to Dto
    MbtiStatusResponseDto mbtiStatusResponseDto = createMbtiStatusResponseDto(
        mbtiStatus.getTypeIE(), mbtiStatus.getTypeSN(),
        mbtiStatus.getTypeTF(), mbtiStatus.getTypePJ());
    return mbtiStatusResponseDto;
  }

  // 4. 삭제 (성향)
  public void deleteMbtiLogical(Long childId) {
    ChildEntity child = getChild(childId);
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    mbtiStatus.setDeleteAt(LocalDateTime.now());
    mbtiStatus.setChildEntity(null);
    mbtiStatusRepository.save(mbtiStatus);
    //childRepository.save(child);
  }

  public void updateMbtiStatus(ChildEntity child, ResponseFeedbackDto responseFeedbackDto) {
    MbtiStatusEntity mbtiStatus = child.getMbtiStatusEntity();
    mbtiStatus.updateMbtiType(
        Math.max(1, Math.min(10, mbtiStatus.getTypeIE()
            + responseFeedbackDto.getFeedbackValue() * responseFeedbackDto.getBookIE()))
        , Math.max(1, Math.min(10, mbtiStatus.getTypeSN()
            + responseFeedbackDto.getFeedbackValue() * responseFeedbackDto.getBookSN()))
        , Math.max(1, Math.min(10, mbtiStatus.getTypeTF()
            + responseFeedbackDto.getFeedbackValue() * responseFeedbackDto.getBookTF()))
        , Math.max(1, Math.min(10, mbtiStatus.getTypePJ()
            + responseFeedbackDto.getFeedbackValue() * responseFeedbackDto.getBookPJ()))
    );

    mbtiStatus.addHistory(MbtiHistoryEntity.builder()
        .bookId(responseFeedbackDto.getBookId())
        .isLike(responseFeedbackDto.getLikeStatus())
        .typeIE(mbtiStatus.getTypeIE())
        .typeSN(mbtiStatus.getTypeSN())
        .typeTF(mbtiStatus.getTypeTF())
        .typePJ(mbtiStatus.getTypePJ()).build());
  }
}
