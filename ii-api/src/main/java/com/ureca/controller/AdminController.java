package com.ureca.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ureca.config.S3Config;
import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookDetail;
import com.ureca.service.AiService;
import com.ureca.service.BookService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/mbtkids")
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  @Autowired private S3Config s3Config;
  @Autowired private BookService bookService;
  @Autowired private AiService aiService;

  @Value("${s3.bucketname}")
  private String bucketName;

  @Value("${s3.book.path}")
  private String bookPath;

  /**
   * @param searchWord 검색어
   * @title 관리자웹 - 도서 전체 목록 조회
   * @description 검색어에 해당하는 도서 조회 목록을 조회한다.
   */
  @GetMapping("/admin/home")
  public String adminBookHome(Model model, @RequestParam(defaultValue = "") String searchWord) {
    List<BookInfo> resBookList = bookService.getBookList(searchWord); // service - 도서 목록 조회
    if (resBookList != null) {
      model.addAttribute("ResBookList", resBookList);
    }
    return "admin/home";
  } // adminBookHome

  /**
   * @param bookId 도서 아이디
   * @title 관리자웹 - 도서 상세 조회
   * @description 선택한 도서의 상세 정보를 조회한다.
   */
  @GetMapping("/admin/detail")
  public String adminBookDetail(
      Model model, @RequestParam(defaultValue = "", required = true) Long bookId) {

    ResBookDetail resBookDetail = new ResBookDetail();

    if (bookId != -1) { // 등록인 경우 bookId = -1
      resBookDetail = bookService.getBookDetail(bookId); // service - 도서 상세 조회
      resBookDetail.setEmptyFlags(false);
    } else {
      resBookDetail.setEmptyFlags(true); // 등록인 경우 빈 객체로 넘김
    }

    model.addAttribute("ResBookDetail", resBookDetail);

    return "admin/detail";
  } // adminBookDetail

  /**
   * @param ReqBookInfo 입력 정보
   * @title 관리자웹 - 도서 수정
   * @description 수정한 도서 정보를 저장한다.
   */
  @PostMapping("/admin/update")
  public String adminBookUpdate(Model model, @ModelAttribute ReqBookInfo reqBookInfo)
      throws IOException {
    String uploadUrl = "";

    // 수정하는 파일이 존재하는 경우
    if (!"".equals(reqBookInfo.getBookImgFile().getOriginalFilename())) {
      // TODO S3 공통 Service로 빼기
      AmazonS3 s3Client = s3Config.s3Client(); // S3 클라이언트 생성
      String fileName =
          "book_" + UUID.randomUUID().toString() + "_img"; // book_랜덤값_img TODO 이미지 이름 ID 값으로 지정

      // S3에 파일 업로드
      try {
        String key = bookPath + fileName;
        s3Client.putObject(
            bucketName,
            key,
            reqBookInfo.getBookImgFile().getInputStream(),
            getObjectMetadata(reqBookInfo.getBookImgFile()));
        URL fileUrl = s3Client.getUrl(bucketName, key);
        uploadUrl = fileUrl.toString();
      } catch (SdkClientException e) {
        throw new IOException("Error uploading file to S3", e);
      }
      // TODO 기존 파일 삭제
    } else {
      uploadUrl = reqBookInfo.getBookImgUrl();
    }

    bookService.updateBookInfo(reqBookInfo, uploadUrl); // service - 도서 정보 업데이트

    return "redirect:/mbtkids/admin/home";
  } // adminBookUpdate

  /**
   * @param ReqBookInfo 입력 정보
   * @title 관리자웹 - 도서 등록
   * @description 입력한 도서 정보를 저장한다.
   */
  @PostMapping("/admin/register")
  public String adminBookRegister(Model model, @ModelAttribute ReqBookInfo reqBookInfo)
      throws IOException {
    String uploadUrl = "";

    // TODO S3 공통 Service로 빼기
    AmazonS3 s3Client = s3Config.s3Client(); // S3 클라이언트 생성
    String fileName = "book_" + UUID.randomUUID().toString() + "_img"; // book_랜덤값_img

    // S3에 파일 업로드
    try {
      String key = bookPath + fileName;
      s3Client.putObject(
          bucketName,
          key,
          reqBookInfo.getBookImgFile().getInputStream(),
          getObjectMetadata(reqBookInfo.getBookImgFile()));
      URL fileUrl = s3Client.getUrl(bucketName, key);
      uploadUrl = fileUrl.toString();
    } catch (SdkClientException e) {
      throw new IOException("Error uploading file to S3", e);
    }

    bookService.saveBookInfo(reqBookInfo, uploadUrl); // service - 도서 정보 추가

    // TODO 이미지 이름 ID 값으로 지정

    return "redirect:/mbtkids/admin/home";
  } // adminBookRegister

  // TODO 공통 서비스로 빼기
  private ObjectMetadata getObjectMetadata(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());
    return objectMetadata;
  } // getObjectMetadata

  /**
   * @param bookId 책 아이디
   * @param title 책 제목
   * @param contents 줄거리
   * @return modelAndView
   * @title AI를 활용한 도서 성향 부여
   * @description 도서 줄거리를 전달하면 성향 분석 결과를 반환해 DB에 등록합니다.
   */
  // http://localhost:8080/mbtkids/admin/book/ai
  @GetMapping("/admin/book/ai")
  public ModelAndView setBookMbti(
      @RequestParam Long bookId,
      @RequestParam String title,
      @RequestParam String contents,
      Model model) {
    // TODO 관리자 로그인 정보 가져오기
    Long userId = 1L;
    // 도서 -> MBTI 추론 API 요청 및 응답
    String resultMbti = aiService.apiBookMbti(title, contents);
    // DB 등록
    aiService.updateBookMbti(userId, bookId, resultMbti);
    // 다시 수정 페이지로 리다이렉트
    ResBookDetail resBookDetail = bookService.getBookDetail(bookId);
    resBookDetail.setEmptyFlags(false);
    ModelAndView modelAndView = new ModelAndView("admin/detail");
    modelAndView.addObject("ResBookDetail", resBookDetail);
    return modelAndView;
  } // setBookMbti

  // 도서 삭제
  @GetMapping("/admin/delete")
  public String adminBookDelete(
      Model model, @RequestParam(defaultValue = "", required = true) Long bookId) {
    int result = bookService.deleteBookInfo(bookId); // service - 도서 삭제
    if (result > 0) {
      logger.info("삭제 성공" + result);
    }

    return "redirect:/mbtkids/admin/home";
  }
}
