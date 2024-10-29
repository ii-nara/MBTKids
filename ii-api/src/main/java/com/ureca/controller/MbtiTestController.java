package com.ureca.controller;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.dto.MbtiInfoResponseDto;
import com.ureca.dto.MbtiStatusResponseDto;
import com.ureca.model.MbtiQuestion;
import com.ureca.model.MbtiQuestionProvider;
import com.ureca.service.MbtiInfoService;
import com.ureca.service.MbtiManagementService;
import com.ureca.service.MbtiTestService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  private final MbtiTestService mbtiTestService;
  private final MbtiInfoService mbtiInfoService;
  private final MbtiManagementService mbtiManagementService;

  public MbtiTestController(
      MbtiTestService mbtiTestService,
      MbtiQuestionProvider mbtiQuestionProvider,
      MbtiInfoService mbtiInfoService,
      MbtiManagementService mbtiManagementService) {
    this.mbtiTestService = mbtiTestService;
    this.mbtiQuestionProvider = mbtiQuestionProvider;
    this.mbtiInfoService = mbtiInfoService;
    this.mbtiManagementService = mbtiManagementService;
  }

  // 1. 질문 조회
  @GetMapping("/test")
  public String getMbtiTest(Model model) {
    List<MbtiQuestion> mbtiQuestions = mbtiQuestionProvider.getMbtiQuestions();
    model.addAttribute("mbtiQuestions", mbtiQuestions);
    model.addAttribute("nowQuestionIdx", 0);
    return "mbti/test";
  }

  // 2. 결과 저장
  @PostMapping("/save")
  public String postMbtiResult(@AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestParam("answers") String answers) {
    Long childId = principalDetails.getChild().getChildId();
    // 답변 처리
    List<Integer> answerList = mbtiTestService.jsonStrToList(answers);
    // MBTI 성향, 강도 계산 및 저장
    mbtiTestService.processMbtiAnswer(childId, answerList);
    return "redirect:/mbtkids/mbti/result";
  }

  // 3. 결과 조회
  @GetMapping("/result")
  public String getMbtiResult(@AuthenticationPrincipal PrincipalDetails principalDetails,
      Model model) {
    // 성향 조회
    Long childId = principalDetails.getChild().getChildId();
    MbtiStatusResponseDto mbtiTestResDto = mbtiManagementService.getMbtiStatus(childId);
    String mbtiType = mbtiTestResDto.getMbtiType();
    int scoreI = mbtiTestResDto.getTypeI(), scoreE = mbtiTestResDto.getTypeE(),
        scoreS = mbtiTestResDto.getTypeS(), scoreN = mbtiTestResDto.getTypeN(),
        scoreT = mbtiTestResDto.getTypeT(), scoreF = mbtiTestResDto.getTypeF(),
        scoreP = mbtiTestResDto.getTypeP(), scoreJ = mbtiTestResDto.getTypeJ();

    model.addAttribute("mbtiType", mbtiType);
    model.addAttribute("scoreI", mbtiTestService.changeRatio(scoreI, scoreE));
    model.addAttribute("scoreE", mbtiTestService.changeRatio(scoreE, scoreI));
    model.addAttribute("scoreS", mbtiTestService.changeRatio(scoreS, scoreN));
    model.addAttribute("scoreN", mbtiTestService.changeRatio(scoreN, scoreS));
    model.addAttribute("scoreT", mbtiTestService.changeRatio(scoreT, scoreF));
    model.addAttribute("scoreF", mbtiTestService.changeRatio(scoreF, scoreT));
    model.addAttribute("scoreP", mbtiTestService.changeRatio(scoreP, scoreJ));
    model.addAttribute("scoreJ", mbtiTestService.changeRatio(scoreJ, scoreP));
    MbtiInfoResponseDto mbtiInfoResponseDto = mbtiInfoService.getMbtiNmInfo(mbtiType);
    model.addAttribute("mbtiInfo", mbtiInfoResponseDto);
    return "mbti/result";
  }
}