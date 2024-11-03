package com.ureca.controller;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ureca.config.S3Config;
import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookDetail;
import com.ureca.entity.BookStatsEntity;
import com.ureca.service.AiService;
import com.ureca.service.BookService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ss.usermodel.Row; // Apache POI의 Row
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  @GetMapping("/admin")
  public String adminLoginForm() {
    return "admin/loginForm";
  }

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
  } // adminBookDelete

  /**
   * @title 도서 통계 조회
   * @description 도서 통계 정보 조회한다.
   * @param startDate 시작일자
   * @param endDate 종료일자
   * @param publisher 출판사명
   * @param bookName 도서명
   */
  @GetMapping("/admin/stats")
  public String adminBookStats(
      Model model,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate,
      @RequestParam(required = false, defaultValue = "") String publisher,
      @RequestParam(required = false, defaultValue = "") String bookName) {
    // 도서 통계 조회
    List<BookStatsEntity> statsList =
        bookService.getBookStatsByStats(startDate, endDate, publisher, bookName);

    // 조회된 통계 리스트가 null이 아닐 경우 모델에 추가
    if (statsList != null) {
      model.addAttribute("StatsList", statsList);
    }

    return "admin/stats"; // 뷰 이름 반환
  } // adminBookStats

  /**
   * @title 도서 통계 엑셀 다운로드
   * @description 도서 통계 조회 결과를 엑셀로 다운로드 받는다.
   * @param startDate 시작일자
   * @param endDate 종료일자
   * @param publisher 출판사명
   * @param bookName 도서명
   */
  @GetMapping("/admin/excel")
  public ResponseEntity<?> exportExcel(
      @RequestParam String startDate,
      @RequestParam String endDate,
      @RequestParam(required = false) String publisher,
      @RequestParam(required = false) String bookName) {

    // TODO Excel Service 로 분리하기
    SXSSFWorkbook workbook = new SXSSFWorkbook(); // 엑셀 파일 생성
    Sheet sheet = workbook.createSheet("도서 통계"); // 시트 생성
    int rowNo = 0;

    // 헤더 row 생성
    Row headerRow = sheet.createRow(rowNo++); // Apache POI의 Row 사용
    headerRow.createCell(0).setCellValue("ID");
    headerRow.createCell(1).setCellValue("도서명");
    headerRow.createCell(2).setCellValue("출판사");
    headerRow.createCell(3).setCellValue("좋아요 개수");
    headerRow.createCell(4).setCellValue("싫어요 개수");
    headerRow.createCell(5).setCellValue("평균 연령대");
    headerRow.createCell(6).setCellValue("평균 성향");
    headerRow.createCell(7).setCellValue("통계 출력 일자");

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      // 데이터 조회
      List<BookStatsEntity> statsList =
          bookService.getBookStatsByStats(
              LocalDate.parse(startDate), LocalDate.parse(endDate), publisher, bookName);

      for (BookStatsEntity stat : statsList) {
        Row row = sheet.createRow(rowNo++);
        row.createCell(0).setCellValue(stat.getStatsId());
        row.createCell(1).setCellValue(stat.getBookName());
        row.createCell(2).setCellValue(stat.getPublisher());
        row.createCell(3).setCellValue(stat.getLikeCnt());
        row.createCell(4).setCellValue(stat.getDisLikeCnt());
        row.createCell(5).setCellValue(stat.getAvgAge());
        row.createCell(6).setCellValue(stat.getAvgMbti());
        row.createCell(7).setCellValue(stat.getStatsAt().toString()); // 날짜 형식 변환 필요할 수 있음
      }

      workbook.write(outputStream);
      workbook.dispose(); // 메모리 절약을 위해 SXSSFWorkbook 사용 후 dispose 호출

      byte[] excelFile = outputStream.toByteArray();

      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Disposition", "attachment; filename=book_stats.xlsx");

      return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);

    } catch (IOException e) {
      // 예외 처리
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  } // exportExcel
}
