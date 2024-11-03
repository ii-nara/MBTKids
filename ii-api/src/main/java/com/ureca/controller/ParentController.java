package com.ureca.controller;

import com.ureca.config.auth.AdminDetails;
import com.ureca.config.auth.PrincipalDetails;
import com.ureca.dto.ChildCreateDto;
import com.ureca.dto.ParentSignUpRequestDto;
import com.ureca.dto.ReqParentAddInfoDto;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.ParentEntity;
import com.ureca.service.ChildService;
import com.ureca.service.ParentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mbtkids")
@RequiredArgsConstructor
public class ParentController {

  private final ParentService parentService;
  private final ChildService childAddService;

  @GetMapping()
  public String home(HttpServletRequest request) {
    HttpSession session = request.getSession(false);

    if (session != null) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null && auth.getPrincipal() instanceof AdminDetails) {
        session.invalidate();
        SecurityContextHolder.clearContext();
      }
    }

    return "parent/home";
  }

  @GetMapping("/oauth/additionalInfo")
  public String addInfoForm() {
    return "parent/additionalForm";
  }

  @PostMapping("/oauth/additionalInfo")
  public String addInfo(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @ModelAttribute ReqParentAddInfoDto parentAddInfoDto) {
    ParentEntity parent = principalDetails.getParent();
    parentService.saveAdditionalInfo(parentAddInfoDto, parent);

    return "redirect:/mbtkids/childSelectOrAdd";
  }

  @GetMapping("/childSelectOrAdd")
  public String loginSuccess(
      Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    ParentEntity parent = principalDetails.getParent();
    model.addAttribute("parent", parent);

    if (principalDetails.getChildId() != null) {
      principalDetails.clearChild();
    }

    List<ChildEntity> children = childAddService.findChildrenByParentId(parent.getParentId());
    model.addAttribute("children", children);

    return "parent/childSelectOrAdd";
  }

  @GetMapping("/child/select/{childId}")
  public String childProfile(
      @PathVariable Long childId,
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      HttpSession session) {
    ChildEntity child = childAddService.findChildById(childId);
    principalDetails.setChild(child);

    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

    return "redirect:/mbtkids/child/profile";
  }

  @GetMapping("/child/profile")
  public String childProfile(
      Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    ChildEntity child = principalDetails.getChild();
    if (child == null) {
      return "redirect:/mbtkids/childSelectOrAdd";
    }
    model.addAttribute("child", child);

    // null이면 성향검사, 아니면 홈화면 이동
    if (child.getMbtiStatusEntity() == null) {
      return "redirect:/mbtkids/mbti/test";
    }
    return "redirect:/mbtkids/home";
    // 기존 : return "parent/childProfile";
  }

  @GetMapping("/child/add")
  public String addChildForm() {
    return "parent/addChild";
  }

  @PostMapping("/child/add")
  public String addChild(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @ModelAttribute ChildCreateDto childCreateDto) {

    ParentEntity parent = principalDetails.getParent();

    childAddService.addChild(parent, childCreateDto);

    return "redirect:/mbtkids/childSelectOrAdd";
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute ParentSignUpRequestDto parentSignUpRequestDto, Model model) {
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
