package com.ureca.controller;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.dto.EventSaveRequestDto;
import com.ureca.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/mbtkids/events")
@Controller
public class EventController {

  private final EventService eventService;

  @GetMapping("/form")
  public String showEventForm(
      Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      return "redirect:/mbtkids";
    }
    Long parentId = principalDetails.getParent().getParentId();
    model.addAttribute("exists", eventService.exist(parentId));
    model.addAttribute("eventSaveRequestDto", new EventSaveRequestDto());
    return "event/form";
  }

  @PostMapping
  public String submitApplication(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @ModelAttribute EventSaveRequestDto eventSaveRequestDto,
      RedirectAttributes redirectAttributes) {
    Long parentId = principalDetails.getParent().getParentId();
    String message = eventService.eventApplication(parentId, eventSaveRequestDto);
    redirectAttributes.addFlashAttribute("successMessage", message);
    return "redirect:/mbtkids/events/form";
  }
}
