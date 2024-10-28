package com.ureca.controller;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.dto.MbtiHistoryResponseDto;
import com.ureca.dto.MbtiInfoResponseDto;
import com.ureca.dto.MbtiStatusResponseDto;
import com.ureca.entity.ChildEntity;
import com.ureca.service.MbtiInfoService;
import com.ureca.service.MbtiManagementService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mbtkids/mbti")
@RequiredArgsConstructor
public class MbtiManagementController {

  private final MbtiManagementService mbtiManagementService;
  private final MbtiInfoService mbtiInfoService;

  @GetMapping("/status")
  public String getStatus(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    ChildEntity child = principalDetails.getChild();
    Long childId = child.getChildId();

    MbtiStatusResponseDto mbtiStatusResponseDto = mbtiManagementService.getMbtiStatus(childId);
    MbtiInfoResponseDto mbtiInfoResponseDto = mbtiInfoService.getMbtiNmInfo(
        mbtiStatusResponseDto.getMbtiType());

    model.addAttribute(child);
    model.addAttribute(mbtiStatusResponseDto);
    model.addAttribute(mbtiInfoResponseDto);
    return "mbti/status";
  }

  @GetMapping("/history")
  public String getHistory(
      @RequestParam(value = "startDate", defaultValue = "#{T(java.time.LocalDate).now().minusWeeks(1)}") LocalDate startDate,
      @RequestParam(value = "endDate", defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate,
      Model model,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Long childId = principalDetails.getChild().getChildId();
    List<MbtiHistoryResponseDto> mbtiHistoryList = mbtiManagementService.getMbtiHistory(childId,
        startDate, endDate);

    if (mbtiHistoryList.isEmpty()) {
      model.addAttribute("noDataAlert", true);
      mbtiHistoryList = mbtiManagementService.getMbtiHistory(childId, LocalDate.now().minusWeeks(1),
          LocalDate.now());
    } else {
      model.addAttribute("noDataAlert", false);
    }
    model.addAttribute("historyList", mbtiHistoryList);
    return "mbti/history";
  }

  @PostMapping("/status")
  public String deleteStatus(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    Long childId = principalDetails.getChild().getChildId();
    mbtiManagementService.deleteMbtiLogical(childId);
    return "redirect:/mbtkids/childSelectOrAdd";
  }

}
