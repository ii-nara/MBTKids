package com.ureca.controller;

import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookInfo;
import com.ureca.service.BookService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mbtkids")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  private BookService bookService;
  public AdminController(BookService bookService) {
    this.bookService = bookService;
  }

  //도서 목록 조회
  @GetMapping("/admin/home")
  public String adminBookHome(Model model, @RequestParam(defaultValue = "") String searchWord) {
    //logger.info("검색어 : "+searchWord);

    // 서비스 호출 - 도서 목록 조회
    List<BookInfo> resBookList = bookService.getBookList(searchWord);

    if (resBookList != null) {
      model.addAttribute("ResBookList", resBookList);
    }
    //logger.info("ResBookList 전달 !" + resBookList);
    return "admin/home";
  }

  //도서 상세 조회
  @GetMapping("/admin/detail")
  public String adminBookDetail(Model model, @RequestParam(defaultValue = "", required = true) Long bookId) {
    // 서비스 호출 - 도서 상세 조회
    ResBookInfo resBookInfo = bookService.getBookInfo(bookId);

    if (resBookInfo != null) {
      model.addAttribute("ResBookInfo", resBookInfo);
    }
    //logger.info("ResBookInfo 전달 !" + resBookInfo);
    return "admin/detail";
  }

  //도서 수정 내용 저장
  @PostMapping("/admin/update")
  public String adminBookUpdate(Model model, @ModelAttribute ReqBookInfo reqBookInfo) {
    //logger.info("입력 : "+reqBookInfo);
    //TODO 저장 로직


    return "redirect:/mbtkids/admin/home";
  }


}
