package com.ureca.controller;

import com.ureca.dto.BookInfo;
import com.ureca.service.RecommendService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    List<BookInfo> similarBooks = recommendService.recommendSimilarBooks(childId, DEFAULT_OFFSET,
        DEFAULT_LIMIT);
    model.addAttribute("similarBooks", similarBooks);
    List<BookInfo> oppositeBooks = recommendService.recommendOppositeBooks(childId, DEFAULT_OFFSET,
        DEFAULT_LIMIT);
    model.addAttribute("oppositionBooks", oppositeBooks);
    List<BookInfo> similarChildLikedBooks = recommendService.recommendSimilarChildLikedBooks(
        childId, DEFAULT_OFFSET, DEFAULT_LIMIT);
    model.addAttribute("similarChildLikedBooks", similarChildLikedBooks);

    model.addAttribute("currentPage", DEFAULT_OFFSET);
    model.addAttribute("pageSize", DEFAULT_LIMIT);

    return "/home";
  }

  @GetMapping("/books")
  public String books(Model model,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    Long childId = 1L;  //TODO session

    List<BookInfo> books = new ArrayList<>();
    String title = "도서 목록";
    if (Objects.equals("SIMILAR", type)) {
      title = "아이 성향 추천";
      books = recommendService.recommendSimilarBooks(childId, page * size, size);
    }
    if (Objects.equals("OPPOSITE", type)) {
      title = "반대 성향 추천";
      books = recommendService.recommendOppositeBooks(childId, page * size, size);
    }
    if (Objects.equals("LIKE", type)) {
      title = "유사 성향 아이들 추천";
      books = recommendService.recommendSimilarChildLikedBooks(childId, page * size, size);
    }
    model.addAttribute("books", books);
    model.addAttribute("title", title);

    // 현재 페이지 번호와 다음 페이지가 있는지 여부를 추가
    model.addAttribute("type", type);
    model.addAttribute("books", books);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", size);
    model.addAttribute("hasNextPage", books.size() == size); // 다음 페이지가 있는지 판단

    return "/book/books";
  }
}
