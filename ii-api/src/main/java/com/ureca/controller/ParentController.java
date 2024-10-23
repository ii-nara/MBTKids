package com.ureca.controller;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.dto.ChildCreateDto;
import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.entity.ParentEntity;
import com.ureca.service.ChildAddServiceImpl;
import com.ureca.service.port.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mbtkids")
@RequiredArgsConstructor
public class ParentController {

  private final ParentService parentService;
  private final ChildAddServiceImpl childAddService;

  @GetMapping()
  public String home() {
    return "parent/home";
  }

  @GetMapping("/success")
  public String loginSuccess(Model model, @AuthenticationPrincipal
  PrincipalDetails principalDetails) {
    ParentEntity parent = principalDetails.getParent();
    model.addAttribute("parent", parent);

    return "parent/childSelectOrAdd";
  }

  @GetMapping("/child/add")
  public String addChildForm() {
    return "parent/addChild";
  }

  @PostMapping("/child/add")
  public String addChild(@AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute
  ChildCreateDto childCreateDto) {

    ParentEntity parent = principalDetails.getParent();

    childAddService.addChild(parent, childCreateDto);

    return "redirect:/mbtkids/success";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute ParentSignUpRequestDto parentSignUpRequestDto,
      Model model) {
    try {
      parentService.create(parentSignUpRequestDto);
      model.addAttribute("message", "회원가입이 완료되었습니다.");

      return "parent/home";
    } catch (IllegalArgumentException e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "parent/home";
    }
  }
}