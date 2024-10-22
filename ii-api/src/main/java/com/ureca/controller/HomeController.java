package com.ureca.controller;

import com.ureca.dto.BookInfo;
import com.ureca.service.RecommendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/mbtkids")
@Controller
public class HomeController {

  private final RecommendService recommendService;

  @GetMapping("/home")
  public String home(Model model) {
    Long childId = 1L;  //TODO session
    List<BookInfo> similarBooks = recommendService.recommendSimilarity(childId);
    model.addAttribute("similarBooks", similarBooks);
    return "/home";
  }
}
