package com.ureca.controller;

import com.ureca.dto.BookInfo;
import com.ureca.service.BookService;
import com.ureca.service.TestService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  public String adminHome(Model model, @RequestParam(defaultValue = "")  String searchWord) {
    //logger.info("검색어 : "+searchWord);

    // 서비스 호출 - 도서 목록 조회
    List<BookInfo> resBookList = bookService.getBookList(searchWord);

    if (resBookList != null) {
      model.addAttribute("ResBookList", resBookList);
    }
    //logger.info("ResBookList 전달 !" + resBookList);
    return "admin/home";
  }

}
