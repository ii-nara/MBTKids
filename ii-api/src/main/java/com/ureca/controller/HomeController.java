package com.ureca.controller;

import com.ureca.constant.RecommendationType;
import com.ureca.dto.BookInfo;
import com.ureca.dto.BookPage;
import com.ureca.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/mbtkids")
@Controller
public class HomeController {

  public static final int DEFAULT_OFFSET = 0;
  public static final int DEFAULT_LIMIT = 10;

  private final RecommendService recommendService;

  @GetMapping("/home")
  public String home(Model model) {
    Long childId = 1L;  //TODO session

    BookPage<BookInfo> similarBooks = recommendService.recommendSimilarBooks(childId,
        DEFAULT_OFFSET,
        DEFAULT_LIMIT);
    BookPage<BookInfo> oppositeBooks = recommendService.recommendOppositeBooks(childId,
        DEFAULT_OFFSET,
        DEFAULT_LIMIT);
    BookPage<BookInfo> likedBooks = recommendService.recommendSimilarChildLikedBooks(
        childId, DEFAULT_OFFSET, DEFAULT_LIMIT);

    model.addAttribute("similarBooks", similarBooks);
    model.addAttribute("oppositeBooks", oppositeBooks);
    model.addAttribute("likedBooks", likedBooks);
    model.addAttribute("currentPage", DEFAULT_OFFSET);
    model.addAttribute("pageSize", DEFAULT_LIMIT);

    return "/home";
  }

  @GetMapping("/books")
  public String books(Model model,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "page", defaultValue = "" + DEFAULT_OFFSET) int page,
      @RequestParam(value = "size", defaultValue = "" + DEFAULT_LIMIT) int size) {
    Long childId = 1L;  //TODO session

    RecommendationType recommendationType;
    try {
      recommendationType = RecommendationType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      return "redirect:/mbtkids/home";
    }

    model.addAttribute("books",
        recommendationType.recommend(recommendService, childId, page, size));
    model.addAttribute("title", recommendationType.getTitle());
    model.addAttribute("type", type);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", size);

    return "/book/books";
  }
}
