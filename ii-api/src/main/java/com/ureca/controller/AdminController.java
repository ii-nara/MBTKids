package com.ureca.controller;

import com.ureca.Enum.MbtiType;
import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookDetail;
import com.ureca.dto.ResMbtiInfo;
import com.ureca.service.AiService;
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
  private AiService aiService;

  public AdminController(BookService bookService, AiService aiService) {
    this.bookService = bookService;
    this.aiService = aiService;
  }

  // 도서 전체 목록 조회
  @GetMapping("/admin/home")
  public String adminBookHome(Model model, @RequestParam(defaultValue = "") String searchWord) {
    // logger.info("검색어 : "+searchWord);

    // 서비스 호출 - 도서 목록 조회
    List<BookInfo> resBookList = bookService.getBookList(searchWord);

    if (resBookList != null) {
      model.addAttribute("ResBookList", resBookList);
    }
    // logger.info("ResBookList 전달 !" + resBookList);
    return "admin/home";
  }

  // 도서 상세 조회
  @GetMapping("/admin/detail")
  public String adminBookDetail(
      Model model, @RequestParam(defaultValue = "", required = true) Long bookId) {

    ResBookDetail resBookDetail = new ResBookDetail();

    if (bookId != -1) {
      // 서비스 호출 - 도서 상세 조회
      resBookDetail = bookService.getBookDetail(bookId);
      resBookDetail.setEmptyFlags(false);
    } else {
      // 도서 등록인 경우 빈 객체로 넘김
      resBookDetail.setEmptyFlags(true);
    }

    model.addAttribute("ResBookDetail", resBookDetail);

    // logger.info("ResBookDetail 전달 !" + resBookDetail);
    return "admin/detail";
  }

  // 도서 수정 내용 저장
  @PostMapping("/admin/update")
  public String adminBookUpdate(Model model, @ModelAttribute ReqBookInfo reqBookInfo) {
    // logger.info("입력 : "+reqBookInfo);
    // TODO 저장 로직

    return "redirect:/mbtkids/admin/home";
  }

  // 도서 등록
  @PostMapping("/admin/register")
  public String adminBookRegister(Model model, @ModelAttribute ReqBookInfo reqBookInfo) {
    logger.info("입력 : " + reqBookInfo);
    // TODO 저장 로직

    return "redirect:/mbtkids/admin/home";
  }

  /**
   * @title AI를 활용한 도서 성향 부여
   * @description 도서 줄거리를 전달하면 성향 분석 결과를 반환합니다.
   * @param contents 줄거리
   * @return ResMbtiInfo
   */
  // http://localhost:8080/mbtkids/admin/book/ai
  @GetMapping("/admin/book/ai")
  public ResMbtiInfo setBookMbti(String contents) {
    String resultMbti = "0000"; // 해당없음 초기화
    String textPJ = "", textTF = "", textSN = "", textIE = "";

    resultMbti = aiService.setBookMbti(contents);

    if (!resultMbti.isEmpty()) {
      textIE = String.valueOf(resultMbti.charAt(0)); // I/E/0
      textSN = String.valueOf(resultMbti.charAt(1)); // S/N/0
      textTF = String.valueOf(resultMbti.charAt(2)); // T/F/0
      textPJ = String.valueOf(resultMbti.charAt(3)); // P/J/0
    }

    ResMbtiInfo resMbtiInfo = new ResMbtiInfo();
    resMbtiInfo.setMbtiType(resultMbti);
    resMbtiInfo.setTypeIE(MbtiType.TYPE_IE.getValueForType(textIE)); // -1/1/0
    resMbtiInfo.setTypeSN(MbtiType.TYPE_SN.getValueForType(textSN)); // -1/1/0
    resMbtiInfo.setTypeTF(MbtiType.TYPE_TF.getValueForType(textTF)); // -1/1/0
    resMbtiInfo.setTypePJ(MbtiType.TYPE_PJ.getValueForType(textPJ)); // -1/1/0

    return resMbtiInfo;
  } // setBookMbti

  // 도서 삭제
  @GetMapping("/admin/delete")
  public String adminBookDelete(
      Model model, @RequestParam(defaultValue = "", required = true) Long bookId) {
    // 서비스 호출 - 도서 삭제
    int result = bookService.deleteBookInfo(bookId);
    if (result > 0) logger.info("삭제 성공" + result);

    return "redirect:/mbtkids/admin/home";
  }
}
