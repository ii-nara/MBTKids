package com.ureca.controller;

import com.ureca.domain.MbtiQuestion;
import com.ureca.domain.MbtiQuestionProvider;
import com.ureca.dto.MbtiResponseDTO;
import com.ureca.service.MbtiTestService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* 자녀 성향 진단 */
@Controller
@RequestMapping("/mbtkids/mbti")
public class MbtiTestController {

  private final MbtiQuestionProvider mbtiQuestionProvider;
  private final MbtiTestService mbtiService;

  public MbtiTestController(
      MbtiTestService mbtiService, MbtiQuestionProvider mbtiQuestionProvider) {
    this.mbtiService = mbtiService;
    this.mbtiQuestionProvider = mbtiQuestionProvider;
  }

  // 1. 질문 조회
  @GetMapping("/test")
  public String getMbtiTest(Model model) {
    List<MbtiQuestion> mbtiQuestions = mbtiQuestionProvider.getMbtiQuestions();
    model.addAttribute("mbtiQuestions", mbtiQuestions);
    model.addAttribute("nowQuestionIdx", 0);
    return "mbti/test";
  }

  // 2. 응답 처리
  @PostMapping("/result")
  public String postMbtiResult(@RequestParam("answers") String answers, Model model) {
    // 답변
    List<Integer> answerList = mbtiService.jsonStrToList(answers);
    // 유효성 확인
    if (answerList.isEmpty()) {
      model.addAttribute("error", "[404] 다시 검사를 시도해주세요.");
      // TODO : 에러 페이지 구현
      return "mbti/error";
    }
    // MBTI 성향, 강도 계산
    MbtiResponseDTO mbtiResponseDTO = mbtiService.processMbtiAnswer(answerList);
    String mbtiType = mbtiResponseDTO.getMBTI();
    List<Integer> scores = mbtiResponseDTO.getScore();
    model.addAttribute("mbtiType", mbtiType);
    model.addAttribute("scores", scores);
    return "mbti/result";
  }
}
