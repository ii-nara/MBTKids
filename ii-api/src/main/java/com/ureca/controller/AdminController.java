package com.ureca.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookInfo;
import com.ureca.service.BookService;
import com.ureca.service.OpenApiService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/mbtkids")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  private BookService bookService;
  private OpenApiService openApiService;
  public AdminController(BookService bookService, OpenApiService openApiService) {
    this.bookService = bookService;
    this.openApiService = openApiService;
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

  // TODO 코드 분리 - 일단 Controller에 다 넣어놓았음
  // @Value("${api.kcisa.serviceKey}")
  // private String serviceKey; // application.properties에서 서비스 키를 가져옵니다.
  // http://localhost:8080/mbtkids/openapi/data
  @GetMapping("/openapi/data")
  public void getOpenApiData() throws IOException {
    String serviceKey = ""; // 인증키
    String numOfRows = "300"; // 세션당 요청레코드수
    String pageNo = "1"; // 페이지 수
    String openApiUrl = "http://api.kcisa.kr/openapi/service/rest/meta2/NLCFsase"; // 오픈 API URL

    String urlText = openApiUrl+"?serviceKey="+serviceKey+"&numOfRows="+numOfRows+"&pageNo="+pageNo;
    URL url = new URL(urlText);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-type", "application/json");
    conn.setRequestProperty("Accept","application/json");
    //System.out.println("Response code: " + conn.getResponseCode());

    BufferedReader rd;
    if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    } else {
      rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line);
    }
    rd.close();
    conn.disconnect();
    //System.out.println(sb.toString());

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(sb.toString());
      // items -> item 배열에 접근
      JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
      // service 호출
      openApiService.setBookList(itemsNode);
    } catch (IOException e) {
      e.printStackTrace();
    }

  } //getOpenApiData

}
