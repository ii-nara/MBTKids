package com.ureca.controller;

import com.ureca.config.auth.PrincipalDetails;
import com.ureca.constant.RecommendationType;
import com.ureca.dto.BookInfo;
import com.ureca.dto.BookPage;
import com.ureca.dto.RequestFeedbackDto;
import com.ureca.dto.ResBookInfo;
import com.ureca.service.BookService;
import com.ureca.service.FeedbackComponentService;
import com.ureca.service.RecommendService;
import lombok.RequiredArgsConstructor;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/mbtkids")
@Controller
public class HomeController {

  public static final int DEFAULT_OFFSET = 0;
  public static final int DEFAULT_LIMIT = 10;

  private final RecommendService recommendService;
  private final BookService bookService;
  private final FeedbackComponentService feedbackComponentService;

  //  private final RabbitTemplate rabbitTemplate;

  @GetMapping("/home")
  public String home(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Long childId = principalDetails.getChild().getChildId();

    BookPage<BookInfo> similarBooks =
        recommendService.recommendSimilarBooks(childId, DEFAULT_OFFSET, DEFAULT_LIMIT);
    BookPage<BookInfo> oppositeBooks =
        recommendService.recommendOppositeBooks(childId, DEFAULT_OFFSET, DEFAULT_LIMIT);
    BookPage<BookInfo> likedBooks =
        recommendService.recommendSimilarChildLikedBooks(childId, DEFAULT_OFFSET, DEFAULT_LIMIT);

    model.addAttribute("similarBooks", similarBooks);
    model.addAttribute("oppositeBooks", oppositeBooks);
    model.addAttribute("likedBooks", likedBooks);
    model.addAttribute("currentPage", DEFAULT_OFFSET);
    model.addAttribute("pageSize", DEFAULT_LIMIT);

    return "/home";
  }

  @GetMapping("/books")
  public String books(
      Model model,
      @RequestParam(value = "type") String type,
      @RequestParam(value = "page", defaultValue = "" + DEFAULT_OFFSET) int page,
      @RequestParam(value = "size", defaultValue = "" + DEFAULT_LIMIT) int size,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    Long childId = principalDetails.getChild().getChildId();

    RecommendationType recommendationType;
    try {
      recommendationType = RecommendationType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      return "redirect:/mbtkids/home";
    }

    model.addAttribute(
        "books", recommendationType.recommend(recommendService, childId, page, size));
    model.addAttribute("title", recommendationType.getTitle());
    model.addAttribute("type", type);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", size);

    return "/book/books";
  }

  // 도서 상세 조회
  @GetMapping("/book/detail")
  public String bookDetail(
      Model model,
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestParam(defaultValue = "", required = true) Long bookId) {
    // 서비스 호출 - 도서 상세 조회
    ResBookInfo resBookInfo = bookService.getBookInfo(bookId);
    resBookInfo.updateLikeStatus(
        feedbackComponentService.findFeedbackStatus(
            bookId, principalDetails.getChild().getChildId()));

    if (resBookInfo != null) {
      model.addAttribute("ResBookInfo", resBookInfo);
    }
    // logger.info("ResBookInfo 전달 !" + resBookInfo);
    return "/book/detail";
  } // bookDetail

  // 도서 좋아요
  @PostMapping("/book/feedback")
  public String pressTheButton(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @RequestBody RequestFeedbackDto requestFeedbackDto) {
    requestFeedbackDto.updateChildId(principalDetails.getChild().getChildId());
    feedbackComponentService.addFeedback(requestFeedbackDto);
    //    rabbitTemplate.convertAndSend("feedbackExchange", "feedbackRoutingKey",
    // requestFeedbackDto);
    return "redirect:/mbtkids/book/detail?bookId=" + requestFeedbackDto.getBookId();
  }
}
