package com.ureca.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.model.MbtiQuestion;
import com.ureca.model.MbtiQuestionProvider;
import com.ureca.dto.MbtiStatusRequestDto;
import com.ureca.dto.MbtiStatusResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/* Mbti 테스트 서비스 */

@Service
public class MbtiTestService {

  private static final Logger logger = LoggerFactory.getLogger(MbtiTestService.class);
  private static final int POSITIVE_THRESHOLD = 2, NEGATIVE_THRESHOLD = 3;
  private static final int MIN_SCORE = 1, MAX_SCORE = 10;
  private static final int INDEX_IE = 0, INDEX_SN = 1, INDEX_TF = 2, INDEX_PJ = 3;

  private final ObjectMapper objectMapper;
  private final MbtiQuestionProvider mbtiQuestionProvider;
  private final MbtiManagementService mbtiManagementService;

  MbtiTestService(MbtiQuestionProvider mbtiQuestionProvider,
      MbtiManagementService mbtiManagementService) {
    this.objectMapper = new ObjectMapper();
    this.mbtiQuestionProvider = mbtiQuestionProvider;
    this.mbtiManagementService = mbtiManagementService;
  }

  // 1. JSON to List<Integer>
  public List<Integer> jsonStrToList(String answers) {
    try {
      return objectMapper.readValue(answers, new TypeReference<List<Integer>>() {
      });
    } catch (JsonProcessingException e) {
      logger.error("JSON parsing error: ", e.getMessage());
      return Collections.emptyList();
    }
  }

  // 2. 초기화
  // MBTI 성향별 강도, 개수 Map
  private Map<String, Integer> initMbtiMap() {
    return new HashMap<>(Map.of("I", 0, "E", 0, "S", 0, "N", 0,
        "T", 0, "F", 0, "P", 0, "J", 0));
  }

  // MBTI 유형별 요소 Map
  private Map<String, String> initPairMap() {
    Map<String, String> pairMap = new LinkedHashMap<>();
    pairMap.put("I", "E");
    pairMap.put("S", "N");
    pairMap.put("T", "F");
    pairMap.put("P", "J");
    return pairMap;
  }

  // 3. 답변 -> 성향별 총 점수 및 개수
  private void calculateSum(List<Integer> answerList, Map<String, Integer> scoreMap,
      Map<String, Integer> countMap) {
    // 질문 목록
    List<MbtiQuestion> questionList = mbtiQuestionProvider.getMbtiQuestions();
    // 모든 응답에 대해
    for (int i = 0; i < questionList.size(); i++) {
      MbtiQuestion question = questionList.get(i);
      // 성향
      String[] types = question.getType().split("/");
      String negativeType = types[0]; // 부정 성향
      String positiveType = types[1]; // 긍정 성향
      // 답변 : 정수 (1~4)
      int answerValue = answerList.get(i);
      // 점수 계산 : 긍정 (1~2), 부정 (3~4)
      int score = (answerValue <= POSITIVE_THRESHOLD) ?
          (NEGATIVE_THRESHOLD - answerValue) : (answerValue - POSITIVE_THRESHOLD); // 강도
      String type = (answerValue <= POSITIVE_THRESHOLD) ? positiveType : negativeType; // MBTI
      // 결과
      scoreMap.put(type, scoreMap.get(type) + score);
      countMap.put(type, countMap.get(type) + 1);
      System.out.println("scoreMap: "+scoreMap.toString());
      System.out.println("countMap: "+countMap.toString());
    }
  }

  // 4. MBTI 성향 계산
  private String calculateMbti(Map<String, Integer> countMap) {
    // 결과
    StringBuilder mbtiResult = new StringBuilder();
    // 각 유형 종류 : <성향, 반대성향>
    Map<String, String> typePairs = initPairMap();
    // 모든 유형에 대해
    for (String typeA : typePairs.keySet()) {
      String typeB = typePairs.get(typeA);
      int typeACnt = countMap.get(typeA);
      int typeBcnt = countMap.get(typeB);
      // 응답 개수로 대표 유형 결정
      String typeTotal = (typeACnt > typeBcnt) ? typeA : typeB;
      mbtiResult.append(typeTotal);
    }
    return mbtiResult.toString();
  }

  // 5. MBTI 강도 계산
  private List<Integer> calculateScore(Map<String, Integer> scoreMap) {
    // 각 유형별 스케일링 결과 : I/E + S/N + T/F + P/J
    List<Integer> scoreList = new ArrayList<>();
    // 각 유형 종류 : <성향, 반대성향>
    Map<String, String> typePairs = initPairMap();
    // 모든 유형에 대해
    for (String typeA : typePairs.keySet()) {
      String typeB = typePairs.get(typeA);
      int typeAScore = scoreMap.get(typeA);
      int typeBScore = scoreMap.get(typeB);
      float sumScoreAB = typeAScore + typeBScore;
      int ratioA = Math.round((typeAScore / sumScoreAB) * MAX_SCORE);
      int ratioB = Math.round((typeBScore / sumScoreAB) * MAX_SCORE);
      if (ratioA > ratioB) {
        scoreList.add((MAX_SCORE - ratioA) + MIN_SCORE);
      } else {
        scoreList.add(ratioB);
      }
    }
    return scoreList;
  }

  // 7. 최종 검사 결과 응답
  public MbtiStatusResponseDto processMbtiAnswer(Long childId, List<Integer> answerList) {
    // MBTI 성향별 강도 : <성향, 강도>
    Map<String, Integer> scoreMap = initMbtiMap();
    // MBTI 성향별 개수 : <성향, 개수>
    Map<String, Integer> countMap = initMbtiMap();
    // (3) 답변 -> 성향별 총 점수 및 개수
    calculateSum(answerList, scoreMap, countMap);
    // (4) MBTI 계산
    String mbti = calculateMbti(countMap);
    System.out.println("mbti: "+mbti);
    // (5) 강도 계산
    List<Integer> scoreList = calculateScore(scoreMap);
    System.out.println("score: "+scoreList.toString());
    // (6) 저장
    MbtiStatusRequestDto requestDto = MbtiStatusRequestDto.builder()
        .mbti(mbti.toString())
        .scoreIE(scoreList.get(INDEX_IE))
        .scoreSN(scoreList.get(INDEX_SN))
        .scoreTF(scoreList.get(INDEX_TF))
        .scorePJ(scoreList.get(INDEX_PJ))
        .build();
    MbtiStatusResponseDto responseDto = mbtiManagementService.insertMbtiStatus(childId, requestDto);
    return responseDto;
  }
}