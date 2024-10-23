package com.ureca.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.domain.MbtiQuestion;
import com.ureca.domain.MbtiQuestionProvider;
import com.ureca.dto.MbtiTestResponseDTO;
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
  private final ObjectMapper objectMapper;
  private final MbtiQuestionProvider mbtiQuestionProvider;

  MbtiTestService(MbtiQuestionProvider mbtiQuestionProvider) {
    this.objectMapper = new ObjectMapper();
    this.mbtiQuestionProvider = mbtiQuestionProvider;
  }

  // 1. JSON -> List<Integer> 변환
  public List<Integer> jsonStrToList(String answers) {
    try {
      return objectMapper.readValue(answers, new TypeReference<List<Integer>>() {
      });
    } catch (JsonProcessingException e) {
      logger.error("JSON parsing error: ", e.getMessage());
      return Collections.emptyList();
    }
  }

  // 2. 답변 처리
  public MbtiTestResponseDTO processMbtiAnswer(List<Integer> answerList) {
    // MBTI 성향별 강도 : <성향, 강도>
    Map<String, Integer> scoreMap =
        new HashMap<>(Map.of("I", 0, "E", 0, "S", 0, "N", 0, "T", 0, "F", 0, "P", 0, "J", 0));
    // MBTI 성향별 개수 : <성향, 개수>
    Map<String, Integer> countMap =
        new HashMap<>(Map.of("I", 0, "E", 0, "S", 0, "N", 0, "T", 0, "F", 0, "P", 0, "J", 0));
    // 3. 성향별 총합 점수 계산
    calculateSum(answerList, scoreMap, countMap);
    // 4. MBTI 계산 + 강도
    return calculateMbti(scoreMap, countMap);
  }

  // 3. 답변 -> 성향별 총합 점수 계산
  private void calculateSum(
      List<Integer> answerList, Map<String, Integer> scoreMap, Map<String, Integer> countMap) {
    // 질문 목록
    List<MbtiQuestion> questionList = mbtiQuestionProvider.getMbtiQuestions();
    // 모든 응답에 대해
    for (int i = 0; i < questionList.size(); i++) {
      MbtiQuestion question = questionList.get(i);
      System.out.println((i + 1) + "번째 질문: " + question.getContent());
      // 성향
      String[] types = question.getType().split("/");
      String negativeType = types[0]; // 부정 성향
      String positiveType = types[1]; // 긍정 성향
      // 답변 : 정수 (1~4)
      int answerValue = answerList.get(i);
      System.out.println();
      // 점수 계산 : 긍정 (1~2), 부정 (3~4)
      int score = (answerValue <= 2) ? (3 - answerValue) : (answerValue - 2); // 강도
      String type = (answerValue <= 2) ? positiveType : negativeType; // MBTI
      System.out.println("answerValue: " + answerValue + ", type: " + type + ", score: " + score);
      // 결과
      scoreMap.put(type, scoreMap.get(type) + score);
      countMap.put(type, countMap.get(type) + 1);
    }
  }

  // 4. 답변 -> MBTI 계산 + 강도
  private MbtiTestResponseDTO calculateMbti(
      Map<String, Integer> scoreMap, Map<String, Integer> countMap) {
    // MBTI 각 유형 종류 : <성향, 반대성향>
    Map<String, String> typePairs = new LinkedHashMap<>();
    typePairs.put("I", "E");
    typePairs.put("S", "N");
    typePairs.put("T", "F");
    typePairs.put("P", "J");
    // MBTI 결과
    StringBuilder mbti = new StringBuilder();
    // 각 유형별 스케일링 : I/E + S/N + T/F + P/J
    List<Integer> scoreList = new ArrayList<>();
    // 모든 유형에 대해
    for (String typeA : typePairs.keySet()) {
      String typeB = typePairs.get(typeA);
      int typeACnt = countMap.get(typeA);
      int typeBcnt = countMap.get(typeB);
      // 응답 개수로 대표 유형 결정
      String typeTotal = (typeACnt > typeBcnt) ? typeA : typeB;
      mbti.append(typeTotal);
      scoreList = calculateScore(scoreMap, typeA, typeB, scoreList);
    }
    // MBTI, 점수
    return MbtiTestResponseDTO.builder().MBTI(mbti.toString()).score(scoreList).build();
  }

  // 5. 답변 -> MBTI 강도 계산
  public List<Integer> calculateScore(
      Map<String, Integer> scoreMap, String typeA, String typeB, List<Integer> scoreList) {

    // 각 유형별 비율
    float sumScoreAB = scoreMap.get(typeA) + scoreMap.get(typeB);
    int ratioA = Math.round((scoreMap.get(typeA) / sumScoreAB) * 10);
    int ratioB = Math.round((scoreMap.get(typeB) / sumScoreAB) * 10);
    // 강도 변환
    if (ratioA > ratioB) {
      scoreList.add((10 - ratioA) + 1);
    } else {
      scoreList.add(ratioB);
    }
    return scoreList;
  }
}
