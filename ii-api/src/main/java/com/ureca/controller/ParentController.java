package com.ureca.controller;

import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.service.port.ParentService;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/register")
  public String signUpForm() {
    return "register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute ParentSignUpRequestDto parentSignUpRequestDto,
      Model model) {
    try {
      parentService.create(parentSignUpRequestDto);
      model.addAttribute("message", "회원가입이 완료되었습니다.");

      return "success";
    } catch (IllegalArgumentException e) {
      model.addAttribute("errorMessage", e.getMessage());

      return "register";
    }
  }
}