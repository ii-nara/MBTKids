package com.ureca.service;

import com.ureca.dto.MbtiInfoResponseDto;
import com.ureca.entity.MbtiInfoEntity;
import com.ureca.repository.MbtiInfoRepository;
import org.springframework.stereotype.Service;

/* MBTI 결과에 출력할 정보 조회 */
@Service
public class MbtiInfoService {

  private final MbtiInfoRepository mbtiInfoRepository;

  public MbtiInfoService(MbtiInfoRepository mbtiInfoRepository) {
    this.mbtiInfoRepository = mbtiInfoRepository;
  }

  // Name 조회
  public MbtiInfoResponseDto getMbtiNmInfo(String mbtiNm) {
    MbtiInfoEntity mbtiInfoEntity = mbtiInfoRepository.findByMbtiNm(mbtiNm);
    return MbtiInfoResponseDto.builder()
        .mbtiNm(mbtiInfoEntity.getMbtiNm())
        .emoji(mbtiInfoEntity.getEmoji())
        .suffix(mbtiInfoEntity.getSuffix())
        .content(mbtiInfoEntity.getContent())
        .tag(mbtiInfoEntity.getTag())
        .build();
  }
}
