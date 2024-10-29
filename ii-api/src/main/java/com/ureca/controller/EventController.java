package com.ureca.controller;

import com.ureca.dto.EventSaveRequestDto;
import com.ureca.service.EventService;
import lombok.RequiredArgsConstructor;
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
  public String showEventForm(Model model) {
    //todo 이미 응모한 사람 버튼 비활성화
    model.addAttribute("eventSaveRequestDto", new EventSaveRequestDto());
    return "event/form";
  }

  @PostMapping
  public String submitApplication(@ModelAttribute EventSaveRequestDto eventSaveRequestDto,
      RedirectAttributes redirectAttributes) {
    //todo session (부모 아이디? 자녀 아이디?)
    eventService.save(eventSaveRequestDto);

    redirectAttributes.addFlashAttribute("successMessage", "응모가 완료되었습니다!");
    return "redirect:/mbtkids/events/form";
  }
}
